package com.example.ai_quotes_app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Quotes::class],
    version = 1
)

abstract class QuotesDatabase: RoomDatabase() {
    abstract val dao: QuotesDao
}