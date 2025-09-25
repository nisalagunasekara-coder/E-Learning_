package com.example.elearning.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Wire to UserManagementActivity, ContentManagementActivity, SystemSettingsActivity
    }
}
