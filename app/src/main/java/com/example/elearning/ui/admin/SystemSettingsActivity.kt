package com.example.elearning.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.data.SettingsRepository
import com.example.elearning.databinding.ActivitySystemSettingsBinding

class SystemSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySystemSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySystemSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchUploads.isChecked = SettingsRepository.allowUploads
        binding.switchMaintenance.isChecked = SettingsRepository.maintenanceMode

        binding.switchUploads.setOnCheckedChangeListener { _, isChecked ->
            SettingsRepository.allowUploads = isChecked
        }
        binding.switchMaintenance.setOnCheckedChangeListener { _, isChecked ->
            SettingsRepository.maintenanceMode = isChecked
        }
    }
}
