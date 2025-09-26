package com.example.elearning.data

import android.net.Uri

object VideoRepository {
    data class VideoItem(val uri: String, val name: String)
    private val items = mutableListOf<VideoItem>()
    fun list(): List<VideoItem> = items
    fun add(uri: Uri) {
        val name = uri.lastPathSegment ?: "Video"
        items.add(0, VideoItem(uri.toString(), name))
        ActivityFeedRepository.add("Lecturer uploaded video: $name")
        ContentRepository.enqueueApproval("Video: $name")
    }
}

object LecturerQuizRepository {
    data class LecturerQuiz(val id: Int, val title: String, val questions: List<String>, val assignedTo: List<String>)
    private val quizzes = mutableListOf<LecturerQuiz>()
    private var nextId = 1

    fun list(): List<LecturerQuiz> = quizzes
    fun create(title: String, questions: List<String>, assignedTo: List<String>): LecturerQuiz {
        val q = LecturerQuiz(nextId++, title, questions, assignedTo)
        quizzes.add(0, q)
        ActivityFeedRepository.add("Lecturer created quiz '$title' for ${assignedTo.size} students")
        return q
    }
}

object ProgressRepository {
    data class StudentProgress(val student: String, val quizzesTaken: Int, val avgScore: Int)
    private val data = mutableListOf(
        StudentProgress("stud1", 3, 72),
        StudentProgress("stud2", 1, 65),
        StudentProgress("stud3", 0, 0)
    )
    fun list(): List<StudentProgress> = data
}
