package com.example.ai_quotes_app.data.api

data class APIresponse(    val id: String?,
                           val model: String?,
                           val output: List<OutputBlock>?)
data class OutputBlock(
    val id: String?,
    val type: String?,
    val status: String?,
    val summary: Any?,
    val role: String?,
    val content: List<OutputContent>?
)

data class OutputContent(
    val type: String?,
    val text: String?
)