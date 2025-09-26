package com.example.elearning.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // TODO: Replace with your real base URL
    @Volatile
    var BASE_URL: String = "https://example.com/" // must end with '/'

    private val gson = GsonBuilder().setLenient().create()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    @Volatile
    private var retrofit: Retrofit? = null

    @Synchronized
    fun setBaseUrl(url: String) {
        // Normalize to ensure trailing slash
        val normalized = if (url.isNotBlank() && !url.endsWith('/')) "$url/" else url
        if (normalized != BASE_URL) {
            BASE_URL = normalized
            // Force rebuild with new base URL on next access
            retrofit = null
        }
    }

    fun getBaseUrl(): String = BASE_URL

    fun quizApi(): QuizApi {
        val instance = retrofit ?: synchronized(this) {
            val built = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            retrofit = built
            built
        }
        return instance.create(QuizApi::class.java)
    }
}
