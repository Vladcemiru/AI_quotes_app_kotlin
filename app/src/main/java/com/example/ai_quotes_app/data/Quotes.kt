package com.example.ai_quotes_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quotes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val prompt: String,
    val character: String,
    val quote: String
)
