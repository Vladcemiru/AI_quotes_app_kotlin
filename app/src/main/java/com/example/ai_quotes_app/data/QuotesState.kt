package com.example.ai_quotes_app.data

import android.R

data class QuotesState (
    val quotes: List<Quotes> = emptyList(),
            val quote: String = "",
            val prompt: String = "",
            val character: String = "",
            val sortType: SortType = SortType.CHRONOLOGICAL,
            val isLoading: Boolean = false
)
