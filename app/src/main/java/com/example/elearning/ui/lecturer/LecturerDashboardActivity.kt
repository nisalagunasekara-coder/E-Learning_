package com.example.elearning.ui.lecturer

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivityLecturerDashboardBinding
import android.widget.Toast
import android.util.Log
import android.view.View

class LecturerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLecturerDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLecturerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to Learning Materials (Videos)
        binding.btnVideoMaterials.setOnClickListener {
            Toast.makeText(this, "Opening Videos...", Toast.LENGTH_SHORT).show()
            try {
                startActivity(Intent(this, VideoMaterialsActivity::class.java))
            } catch (e: Exception) {
                Log.e("LecturerDashboard", "Failed to open VideoMaterialsActivity", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Navigate to Create/Assign Quizzes
        binding.btnCreateQuiz.setOnClickListener {
            Toast.makeText(this, "Opening Create Quiz...", Toast.LENGTH_SHORT).show()
            try {
                startActivity(Intent(this, CreateQuizActivity::class.java))
            } catch (e: Exception) {
                Log.e("LecturerDashboard", "Failed to open CreateQuizActivity", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Navigate to Student Progress
        binding.btnStudentProgress.setOnClickListener {
            Toast.makeText(this, "Opening Student Progress...", Toast.LENGTH_SHORT).show()
            try {
                startActivity(Intent(this, StudentProgressActivity::class.java))
            } catch (e: Exception) {
                Log.e("LecturerDashboard", "Failed to open StudentProgressActivity", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // XML onClick handlers
    fun onOpenVideos(@Suppress("UNUSED_PARAMETER") v: View) {
        Toast.makeText(this, "Opening Videos...", Toast.LENGTH_SHORT).show()
        try {
            startActivity(Intent(this, VideoMaterialsActivity::class.java))
        } catch (e: Exception) {
            Log.e("LecturerDashboard", "Failed to open VideoMaterialsActivity", e)
            Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun onOpenCreateQuiz(@Suppress("UNUSED_PARAMETER") v: View) {
        Toast.makeText(this, "Opening Create Quiz...", Toast.LENGTH_SHORT).show()
        try {
            startActivity(Intent(this, CreateQuizActivity::class.java))
        } catch (e: Exception) {
            Log.e("LecturerDashboard", "Failed to open CreateQuizActivity", e)
            Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun onOpenStudentProgress(@Suppress("UNUSED_PARAMETER") v: View) {
        Toast.makeText(this, "Opening Student Progress...", Toast.LENGTH_SHORT).show()
        try {
            startActivity(Intent(this, StudentProgressActivity::class.java))
        } catch (e: Exception) {
            Log.e("LecturerDashboard", "Failed to open StudentProgressActivity", e)
            Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
