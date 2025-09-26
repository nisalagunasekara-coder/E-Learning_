package com.example.elearning.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QuizEntity::class, QuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}
