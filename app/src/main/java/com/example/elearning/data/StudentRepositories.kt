package com.example.elearning.data

import android.content.Context
import android.net.Uri
import com.example.elearning.model.ActivityLog

object ActivityFeedRepository {
    private val _logs = mutableListOf<ActivityLog>()
    val logs: List<ActivityLog> get() = _logs
    fun add(description: String) {
        _logs.add(0, ActivityLog(System.currentTimeMillis(), description))
        if (_logs.size > 50) _logs.removeLast()
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

    private val quizzes = mutableListOf<Quiz>()
    private var nextId = 1

    fun list(): List<Quiz> = quizzes

    fun get(id: Int): Quiz? = quizzes.firstOrNull { it.id == id }

    fun generateQuizFromPdf(pdfUri: String, title: String = "Generated Quiz"): Quiz {
        val q = listOf(
            Question(1, "What is the main topic of the document?", listOf("Math", "Science", "History", "Literature"), 1),
            Question(2, "Which section discusses key concepts?", listOf("Intro", "Methodology", "Conclusion", "Abstract"), 0),
            Question(3, "True about PDFs?", listOf("Editable by default", "Preserves layout", "Always encrypted", "Only images"), 1),
            Question(4, "How many examples were provided?", listOf("1", "2", "3", "4"), 2),
            Question(5, "Best way to study this material?", listOf("Skim", "Read thoroughly", "Ignore", "Guess"), 1)
        )
        val quiz = Quiz(nextId++, title, q)
        quizzes.add(0, quiz)
        ActivityFeedRepository.add("Generated quiz from PDF (${title})")
        return quiz
    }
}

object GamesRepository {
    data class Scores(var mcq: Int = 0, var tf: Int = 0, var memory: Int = 0)
    val scores = Scores()
    fun addMcqScore(delta: Int) { scores.mcq += delta; ActivityFeedRepository.add("MCQ Challenge score +$delta (total ${scores.mcq})") }
    fun addTfScore(delta: Int) { scores.tf += delta; ActivityFeedRepository.add("True/False score +$delta (total ${scores.tf})") }
    fun addMemoryScore(delta: Int) { scores.memory += delta; ActivityFeedRepository.add("Memory Match score +$delta (total ${scores.memory})") }
}
