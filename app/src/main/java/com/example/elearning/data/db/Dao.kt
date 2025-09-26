package com.example.elearning.data.db

import androidx.room.*

@Dao
interface QuizDao {
    @Insert
    fun insertQuiz(quiz: QuizEntity): Long

    @Insert
    fun insertQuestions(questions: List<QuestionEntity>)

    @Transaction
    @Query("SELECT * FROM quizzes ORDER BY id DESC")
    fun getAllQuizzesWithQuestions(): List<QuizWithQuestions>

    @Transaction
    @Query("SELECT * FROM quizzes WHERE id = :id LIMIT 1")
    fun getQuizWithQuestions(id: Int): QuizWithQuestions?
}

data class QuizWithQuestions(
    @Embedded val quiz: QuizEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizId"
    )
    val questions: List<QuestionEntity>
)
