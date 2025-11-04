package com.example.ai_quotes_app.data

sealed interface QuoteEvent {
    object InsertQuote: QuoteEvent
    data class SetQuote(val quote: String ): QuoteEvent
    data class SetPrompt(val prompt: String): QuoteEvent
    data class SetCharacter(val character: String): QuoteEvent

    data class DeleteQuote(val quote: Quotes): QuoteEvent
    data class SortQuotes(val sortType: SortType): QuoteEvent
}