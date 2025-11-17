package com.example.ai_quotes_app.data.api

import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.openai.com/v1/"

    fun getService(apiKey: String): ApiInterface {
        return retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $apiKey")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
            .create(ApiInterface::class.java)
    }
}
