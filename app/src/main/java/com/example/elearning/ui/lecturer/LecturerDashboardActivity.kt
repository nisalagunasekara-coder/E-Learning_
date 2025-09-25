package com.example.elearning.ui.lecturer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivityLecturerDashboardBinding

class LecturerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLecturerDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLecturerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Wire to VideoMaterialsActivity, CreateQuizActivity, StudentProgressActivity
    }
}
