package com.example.elearning.ui.admin

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivityAdminDashboardBinding
import android.widget.Toast
import android.util.Log

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // User Management
        binding.btnUserManagement.setOnClickListener {
            Toast.makeText(this, "Opening User Management...", Toast.LENGTH_SHORT).show()
            try {
                startActivity(Intent(this, UserManagementActivity::class.java))
            } catch (e: Exception) {
                Log.e("AdminDashboard", "Failed to open UserManagementActivity", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Content Management
        binding.btnContentManagement.setOnClickListener {
            Toast.makeText(this, "Opening Content Management...", Toast.LENGTH_SHORT).show()
            try {
                startActivity(Intent(this, ContentManagementActivity::class.java))
            } catch (e: Exception) {
                Log.e("AdminDashboard", "Failed to open ContentManagementActivity", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // System Settings
        binding.btnSystemSettings.setOnClickListener {
            Toast.makeText(this, "Opening System Settings...", Toast.LENGTH_SHORT).show()
            try {
                startActivity(Intent(this, SystemSettingsActivity::class.java))
            } catch (e: Exception) {
                Log.e("AdminDashboard", "Failed to open SystemSettingsActivity", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
