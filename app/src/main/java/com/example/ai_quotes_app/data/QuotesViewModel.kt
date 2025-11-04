package com.example.ai_quotes_app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuotesViewModel(
    private val dao: QuotesDao
): ViewModel() {

    private val  _state = MutableStateFlow(QuotesState())
    val state: StateFlow<QuotesState> = _state

    fun onEvent(event: QuoteEvent){
        when(event){
            is QuoteEvent.DeleteQuote -> {
                viewModelScope.launch {
                    dao.deleteQuote(event.quote);
                }
            }
            is QuoteEvent.SetQuote -> {
                _state.update {it.copy(
                    quote = event.quote
                )}
            }

            QuoteEvent.InsertQuote -> {
                val qq = state.value.quote

                val quote = Quotes(
                    quote= qq
                )

                viewModelScope.launch {
                    dao.insertQuote(quote)
                }
            }
        }
    }
}