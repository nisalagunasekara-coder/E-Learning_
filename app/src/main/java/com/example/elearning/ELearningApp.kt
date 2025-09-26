package com.example.elearning

import android.app.Application
import com.example.elearning.data.db.DatabaseProvider
import com.example.elearning.settings.SettingsRepository
import com.example.elearning.network.ApiClient

class ELearningApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Room once for the whole app
        DatabaseProvider.init(this)

        // Load saved base URL and apply to ApiClient at startup
        val repo = SettingsRepository(this)
        val savedUrl = repo.getBaseUrl()
        if (!savedUrl.isNullOrBlank()) {
            ApiClient.setBaseUrl(savedUrl)
        }
    }
}
