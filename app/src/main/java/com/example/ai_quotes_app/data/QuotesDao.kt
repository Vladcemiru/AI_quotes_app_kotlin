package com.example.ai_quotes_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuotesDao {
    @Insert
    suspend fun insertQuote(quotes: Quotes)

    @Delete
    suspend fun deleteQuote(quotes: Quotes)

    @Query("SELECT * FROM Quotes ORDER BY id ASC")
    fun getAllQuotes(): Flow<List<Quotes>>
}