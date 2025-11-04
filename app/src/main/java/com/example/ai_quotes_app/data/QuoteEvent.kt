package com.example.ai_quotes_app.data

sealed interface QuoteEvent {
    object InsertQuote: QuoteEvent
    data class SetQuote(val quote: String ): QuoteEvent

    data class DeleteQuote(val quote: Quotes): QuoteEvent
}