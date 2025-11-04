package com.example.ai_quotes_app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuotesViewModel(
    private val dao: QuotesDao
): ViewModel() {


    private val _sortType = MutableStateFlow(SortType.QUOTE)
    private val _quotes = _sortType.
    flatMapLatest { sortType ->
        when(sortType) {
        SortType.CHARACTER -> dao.getQuotesOrderedByCharacter()
        SortType.PROMPT -> dao.getQuotesOrderedByPrompt()
        SortType.QUOTE -> dao.getQuotesOrderedByQuotes()
    } }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(QuotesState())

    // val state: StateFlow<QuotesState> = _state  /insted of this ... that
    val state =  combine(_state, _sortType, _quotes){ state, sortType, quotes -> state.copy(
            quotes = quotes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuotesState())


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
                val prompt = state.value.prompt
                val character = state.value.character
                val qq = state.value.quote

              //if(qq.isBlank() || prompt.isBlank() || character.isBlank()){
              //     return
              //}

                val quote = Quotes(
                    prompt = prompt,
                    character = character,
                    quote = qq
                )
            //call the actual insert
                viewModelScope.launch {
                    dao.upsertQuote(quote)
                }
                _state.update { it.copy(
                    character = "",
                    prompt = "",
                    quote = ""
                ) }

            }

            is QuoteEvent.SetCharacter -> {
                _state.update {it.copy(
                    character = event.character
                )}
            }
            is QuoteEvent.SetPrompt -> {
                _state.update {it.copy(
                    prompt = event.prompt
                )}
            }
            is QuoteEvent.SortQuotes -> {
                _sortType.value = event.sortType
            }
        }
    }
}