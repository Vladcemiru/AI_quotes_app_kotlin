package com.example.ai_quotes_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuotesDao {
    @Upsert
    suspend fun upsertQuote(quotes: Quotes)

    @Delete
    suspend fun deleteQuote(quotes: Quotes)

    @Query("SELECT * FROM Quotes ORDER BY quote ASC")
    fun getQuotesOrderedByQuotes(): Flow<List<Quotes>>

    @Query("SELECT * FROM Quotes ORDER BY prompt ASC")
    fun getQuotesOrderedByPrompt(): Flow<List<Quotes>>

    @Query("SELECT * FROM Quotes ORDER BY character ASC")
    fun getQuotesOrderedByCharacter(): Flow<List<Quotes>>
}