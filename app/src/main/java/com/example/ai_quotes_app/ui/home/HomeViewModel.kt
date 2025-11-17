    package com.example.ai_quotes_app.ui.home

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.ai_quotes_app.data.QuoteEvent
    import com.example.ai_quotes_app.data.QuotesState
    import com.example.ai_quotes_app.data.api.APIrequest
    import com.example.ai_quotes_app.data.api.ApiClient
    import com.example.ai_quotes_app.data.api.ApiInterface
    import kotlinx.coroutines.launch

    class HomeViewModel : ViewModel() {

        private val _text = MutableLiveData<String>().apply {
            value = "MAIN VIEW"
        }
        val text: LiveData<String> = _text


    }


