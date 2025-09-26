package com.example.elearning.data

import android.content.Context
import android.content.ContentResolver
import android.net.Uri
import com.example.elearning.model.ActivityLog
import com.example.elearning.data.db.DatabaseProvider
import com.example.elearning.data.db.QuestionEntity
import com.example.elearning.data.db.QuizEntity
import com.example.elearning.data.db.QuizWithQuestions
import com.google.gson.Gson
import com.example.elearning.network.ApiClient
import com.example.elearning.network.QuizResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

object ActivityFeedRepository {
    private val _logs = mutableListOf<ActivityLog>()
    val logs: List<ActivityLog> get() = _logs
    fun add(description: String) {
        _logs.add(0, ActivityLog(System.currentTimeMillis(), description))
        if (_logs.size > 50) _logs.removeAt(_logs.lastIndex)
    }
}

object PdfRepository {
    data class PdfItem(val uri: String, val name: String)
    private val items = mutableListOf<PdfItem>()
    fun list(): List<PdfItem> = items
    fun add(context: Context, uri: Uri) {
        // Try to get display name; fallback to last path segment
        val name = runCatching {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex("_display_name")
                if (nameIndex >= 0 && cursor.moveToFirst()) cursor.getString(nameIndex) else uri.lastPathSegment ?: "PDF"
            } ?: (uri.lastPathSegment ?: "PDF")
        }.getOrDefault(uri.lastPathSegment ?: "PDF")
        items.add(0, PdfItem(uri.toString(), name))
        ActivityFeedRepository.add("Uploaded PDF: $name")
    }

    fun remove(uri: String): Boolean {
        val idx = items.indexOfFirst { it.uri == uri }
        if (idx >= 0) {
            val removed = items.removeAt(idx)
            ActivityFeedRepository.add("Deleted PDF: ${removed.name}")
            return true
        }
        return false
    }
}

object QuizRepository {
    data class Question(
        val id: Int,
        val text: String,
        val options: List<String>,
        val correctIndex: Int
    )

    data class Quiz(
        val id: Int,
        val title: String,
        val questions: List<Question>
    )

    private val gson = Gson()

    fun list(): List<Quiz> {
        val dao = DatabaseProvider.db.quizDao()
        return dao.getAllQuizzesWithQuestions().map { it.toDomain() }
    }

    fun get(id: Int): Quiz? {
        val dao = DatabaseProvider.db.quizDao()
        return dao.getQuizWithQuestions(id)?.toDomain()
    }

    fun generateQuizFromPdf(@Suppress("UNUSED_PARAMETER") pdfUri: String, title: String = "Generated Quiz"): Quiz {
        // Mocked question generation from PDF
        val mocked = listOf(
            Question(0, "What is the main topic of the document?", listOf("Math", "Science", "History", "Literature"), 1),
            Question(0, "Which section discusses key concepts?", listOf("Intro", "Methodology", "Conclusion", "Abstract"), 0),
            Question(0, "True about PDFs?", listOf("Editable by default", "Preserves layout", "Always encrypted", "Only images"), 1),
            Question(0, "How many examples were provided?", listOf("1", "2", "3", "4"), 2),
            Question(0, "Best way to study this material?", listOf("Skim", "Read thoroughly", "Ignore", "Guess"), 1)
        )

        val dao = DatabaseProvider.db.quizDao()
        val quizId = dao.insertQuiz(QuizEntity(title = title)).toInt()

        val qEntities = mocked.map { q ->
            QuestionEntity(
                quizId = quizId,
                text = q.text,
                optionsJson = gson.toJson(q.options),
                correctIndex = q.correctIndex
            )
        }
        dao.insertQuestions(qEntities)

        val created = get(quizId)!!
        ActivityFeedRepository.add("Generated quiz from PDF (${created.title})")
        return created
    }

    private fun QuizWithQuestions.toDomain(): Quiz {
        val questions = this.questions.map { qe ->
            Question(
                id = qe.id,
                text = qe.text,
                options = gson.fromJson(qe.optionsJson, List::class.java).map { it.toString() },
                correctIndex = qe.correctIndex
            )
        }
        return Quiz(id = this.quiz.id, title = this.quiz.title, questions = questions)
    }

    suspend fun generateQuizFromPdfApi(context: Context, pdf: Uri, title: String? = null): Quiz {
        // If BASE_URL is not configured, deliberately fail fast to use local fallback
        if (com.example.elearning.network.ApiClient.BASE_URL.contains("example.com")) {
            throw IllegalStateException("BASE_URL not configured")
        }
        // Copy content to a cache file because Retrofit needs a File for multipart
        val cacheFile = copyUriToCache(context.contentResolver, pdf)
        val mediaType = "application/pdf".toMediaTypeOrNull()
        val fileBody = cacheFile.asRequestBody(mediaType)
        val filePart = MultipartBody.Part.createFormData("file", cacheFile.name, fileBody)
        val titleBody: RequestBody? = title?.toRequestBody("text/plain".toMediaTypeOrNull())

        val api = ApiClient.quizApi()
        val response: QuizResponse = api.generateQuiz(filePart, titleBody)

        // Ensure exactly up to 5 questions are stored
        val limitedQuestions = response.questions.take(5)

        // Persist to Room
        val dao = DatabaseProvider.db.quizDao()
        val quizId = dao.insertQuiz(QuizEntity(title = response.title)).toInt()
        val qEntities = limitedQuestions.map { q ->
            QuestionEntity(
                quizId = quizId,
                text = q.text,
                optionsJson = gson.toJson(q.options),
                correctIndex = q.correctIndex
            )
        }
        dao.insertQuestions(qEntities)
        val created = get(quizId)!!
        ActivityFeedRepository.add("Generated quiz from PDF via API (${created.title})")
        return created
    }

    private fun copyUriToCache(cr: ContentResolver, uri: Uri): File {
        val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "upload.pdf"
        val outFile = File.createTempFile(fileName.substringBefore('.'), ".pdf")
        cr.openInputStream(uri).use { input ->
            FileOutputStream(outFile).use { output ->
                if (input != null) input.copyTo(output)
            }
        }
        return outFile
    }
}

object GamesRepository {
    data class Scores(var mcq: Int = 0, var tf: Int = 0, var memory: Int = 0)
    val scores = Scores()
    fun addMcqScore(delta: Int) { scores.mcq += delta; ActivityFeedRepository.add("MCQ Challenge score +$delta (total ${scores.mcq})") }
    fun addTfScore(delta: Int) { scores.tf += delta; ActivityFeedRepository.add("True/False score +$delta (total ${scores.tf})") }
    fun addMemoryScore(delta: Int) { scores.memory += delta; ActivityFeedRepository.add("Memory Match score +$delta (total ${scores.memory})") }
}
