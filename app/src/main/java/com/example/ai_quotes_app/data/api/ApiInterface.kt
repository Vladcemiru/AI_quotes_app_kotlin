package com.example.ai_quotes_app.data.api

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers(
        "Content-Type: application/json"
    )
    @POST("responses")
    suspend fun getResponse(
        @Body request: APIrequest
    ): APIresponse
}