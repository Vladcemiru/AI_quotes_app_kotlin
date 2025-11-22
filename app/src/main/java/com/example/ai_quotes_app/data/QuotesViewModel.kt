package com.example.ai_quotes_app.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_quotes_app.data.api.APIrequest
import com.example.ai_quotes_app.data.api.ApiClient
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
    fun apiCall(tone: String) {
        val state = state.value
        _state.update { it.copy(isLoading = true) }
        var text = "Empty response"
        viewModelScope.launch {
            try {
                val service = ApiClient.getService(openAIkey)

                val finalPrompt = """
                    Generate a quote based on the following parameters. Please answer just the quote.
                    Character: ${state.character}
                    Tone: $tone
                    Context: ${state.prompt}
                """.trimIndent()

                val response = service.getResponse(
                    APIrequest(
                        model = "gpt-5-nano",
                        input = finalPrompt
                    )
                )

                 text =
                    response.output
                        ?.firstOrNull { it.type == "message" }
                        ?.content
                        ?.firstOrNull { it.type == "output_text" }
                        ?.text
                        ?: "Empty response"

                val quoteEntity = Quotes(
                    prompt = state.prompt,
                    character = state.character,
                    quote = text
                )
                dao.upsertQuote(quoteEntity)
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
            }  finally {
                onEvent(QuoteEvent.SetQuote(text))
                _state.update { it.copy(isLoading = false) }
                _state.update { it.copy(prompt = "") }

            }
        }
    }
}