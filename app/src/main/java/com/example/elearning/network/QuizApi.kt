package com.example.elearning.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface QuizApi {
    // Default contract; adjust path/parts to your backend
    @Multipart
    @POST("generate-quiz")
    suspend fun generateQuiz(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody?
    ): QuizResponse
}

data class QuizResponse(
    val title: String,
    val questions: List<QuestionResponse>
)

data class QuestionResponse(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)
