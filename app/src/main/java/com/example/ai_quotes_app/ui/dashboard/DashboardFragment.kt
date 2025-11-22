package com.example.ai_quotes_app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ai_quotes_app.MainActivity
import com.example.ai_quotes_app.data.Quotes
import com.example.ai_quotes_app.data.QuotesViewModel
import com.example.ai_quotes_app.ui.home.DarkColors   // ← vezmeme barvy z HomeFragmentu

class DashboardFragment : Fragment() {

    private val viewModel: QuotesViewModel by activityViewModels {
        val mainActivity = requireActivity() as MainActivity
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuotesViewModel(mainActivity.db.dao) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(colorScheme = DarkColors) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                            .padding(bottom = 70.dp),
                        color = DarkColors.background
                    ) {
                        SavedQuotesScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SavedQuotesScreen(viewModel: QuotesViewModel) {
    val state by viewModel.state.collectAsState()

    val quotes: List<Quotes> = state.quotes

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(quotes) { quote ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF22344D)   // stejná karta jako v HomeFragmentu
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text(
                        text = quote.quote,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Character: ${quote.character}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )

                    Text(
                        text = "Prompt: ${quote.prompt}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}
