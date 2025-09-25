package com.example.elearning.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.AuthRepository
import com.example.elearning.databinding.ActivityLoginBinding
import com.example.elearning.model.Role
import com.example.elearning.ui.admin.AdminDashboardActivity
import com.example.elearning.ui.lecturer.LecturerDashboardActivity
import com.example.elearning.ui.student.StudentDashboardActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roles = listOf("Admin", "Lecturer", "Student")
        binding.spinnerRole.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        binding.btnLogin.setOnClickListener {
            val role = when (binding.spinnerRole.selectedItemPosition) {
                0 -> Role.ADMIN
                1 -> Role.LECTURER
                else -> Role.STUDENT
            }
            val username = binding.etUsername.text?.toString()?.trim().orEmpty()
            val password = binding.etPassword.text?.toString()?.trim().orEmpty()

            val user = AuthRepository.login(role, username, password)
            if (user != null) {
                when (user.role) {
                    Role.ADMIN -> startActivity(Intent(this, AdminDashboardActivity::class.java))
                    Role.LECTURER -> startActivity(Intent(this, LecturerDashboardActivity::class.java))
                    Role.STUDENT -> startActivity(Intent(this, StudentDashboardActivity::class.java))
                }
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
