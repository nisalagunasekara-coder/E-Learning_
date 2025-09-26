package com.example.elearning.data.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var initialized = false
    lateinit var db: AppDatabase
        private set

    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "elearning.db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() // For simplicity in this sample app; use coroutines in production
                .build()
            initialized = true
        }
    }
}
