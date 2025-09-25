package com.example.elearning.ui.student

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivityStudentDashboardBinding
import com.example.elearning.ui.common.setupStudentBottomNav
import com.example.elearning.R

class StudentDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to PDF Materials
        binding.btnPdfMaterials.setOnClickListener {
            startActivity(Intent(this, PdfMaterialsActivity::class.java))
        }

        // TODO: Wire up buttons for quizzes and games hubs

        // Bottom navigation
        setupStudentBottomNav(binding.bottomNav.bottomNav, R.id.nav_home)
    }
}
