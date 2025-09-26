package com.example.elearning.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elearning.databinding.ActivitySettingsBinding
import com.example.elearning.network.ApiClient
import com.example.elearning.settings.SettingsRepository

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = SettingsRepository(this)

        // Prefill with current value (from ApiClient or stored value)
        val current = repo.getBaseUrl() ?: ApiClient.getBaseUrl()
        binding.editBaseUrl.setText(current)

        binding.btnSave.setOnClickListener {
            val input = binding.editBaseUrl.text?.toString()?.trim().orEmpty()
            if (input.isBlank()) {
                Toast.makeText(this, "Base URL cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Persist then apply globally
            repo.setBaseUrl(input)
            ApiClient.setBaseUrl(input)
            Toast.makeText(this, "Base URL saved", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnCancel.setOnClickListener { finish() }
    }
}
