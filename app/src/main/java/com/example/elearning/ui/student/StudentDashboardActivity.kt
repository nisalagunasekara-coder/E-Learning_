package com.example.elearning.ui.student

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivityStudentDashboardBinding
import com.example.elearning.ui.common.setupStudentBottomNav
import com.example.elearning.R
import android.util.Log
import android.widget.Toast
import android.view.View

class StudentDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, "Student Dashboard ready", Toast.LENGTH_SHORT).show()

        // Navigate to PDF Materials
        binding.btnPdfMaterials.setOnClickListener {
            startActivity(Intent(this, PdfMaterialsActivity::class.java))
        }

        // Navigate to Quizzes hub
        binding.btnQuizzes.isEnabled = true
        binding.btnQuizzes.isClickable = true
        binding.btnQuizzes.setOnClickListener {
            Toast.makeText(this, "Opening Quizzes...", Toast.LENGTH_SHORT).show()
            Log.d("StudentDashboard", "btnQuizzes clicked, launching QuizHubActivity")
            try {
                startActivity(Intent(this, QuizHubActivity::class.java))
            } catch (e: Exception) {
                Log.e("StudentDashboard", "Failed to open QuizHubActivity", e)
                Toast.makeText(this, "Failed to open Quizzes: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Navigate to Educational Games hub
        binding.btnGames.setOnClickListener {
            Toast.makeText(this, "Opening Games...", Toast.LENGTH_SHORT).show()
            Log.d("StudentDashboard", "btnGames clicked, launching GamesHubActivity")
            try {
                startActivity(Intent(this, GamesHubActivity::class.java))
            } catch (e: Exception) {
                Log.e("StudentDashboard", "Failed to open GamesHubActivity", e)
                Toast.makeText(this, "Failed to open Games: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Also bind via findViewById in case the binding reference differs from inflated layout
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnQuizzes)?.let { btn ->
            btn.isEnabled = true
            btn.isClickable = true
            btn.setOnClickListener {
                Toast.makeText(this, "Opening Quizzes...", Toast.LENGTH_SHORT).show()
                Log.d("StudentDashboard", "btnQuizzes (findViewById) clicked, launching QuizHubActivity")
                try {
                    startActivity(Intent(this, QuizHubActivity::class.java))
                } catch (e: Exception) {
                    Log.e("StudentDashboard", "Failed to open QuizHubActivity (findViewById)", e)
                    Toast.makeText(this, "Failed to open Quizzes: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Fallback wiring for Games button as well
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGames)?.let { btn ->
            btn.isEnabled = true
            btn.isClickable = true
            btn.setOnClickListener {
                Toast.makeText(this, "Opening Games...", Toast.LENGTH_SHORT).show()
                Log.d("StudentDashboard", "btnGames (findViewById) clicked, launching GamesHubActivity")
                try {
                    startActivity(Intent(this, GamesHubActivity::class.java))
                } catch (e: Exception) {
                    Log.e("StudentDashboard", "Failed to open GamesHubActivity (findViewById)", e)
                    Toast.makeText(this, "Failed to open Games: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Bottom navigation
        setupStudentBottomNav(binding.bottomNav.bottomNav, R.id.nav_home)
    }

    // XML onClick fallback
    fun onQuizzesClick(v: View) {
        Toast.makeText(this, "Opening Quizzes...", Toast.LENGTH_SHORT).show()
        Log.d("StudentDashboard", "onQuizzesClick called, launching QuizHubActivity")
        try {
            startActivity(Intent(this, QuizHubActivity::class.java))
        } catch (e: Exception) {
            Log.e("StudentDashboard", "Failed to open QuizHubActivity (xml onClick)", e)
            Toast.makeText(this, "Failed to open Quizzes: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
