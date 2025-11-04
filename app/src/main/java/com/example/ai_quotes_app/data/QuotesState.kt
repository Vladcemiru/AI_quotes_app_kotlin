package com.example.ai_quotes_app.data

data class QuotesState (
    val quotes: List<Quotes> = emptyList(),
            val quote: String = ""
)
