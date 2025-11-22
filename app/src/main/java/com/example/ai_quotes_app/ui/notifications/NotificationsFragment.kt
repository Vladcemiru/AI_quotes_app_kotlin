package com.example.ai_quotes_app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ai_quotes_app.MainActivity
import com.example.ai_quotes_app.data.QuoteEvent
import com.example.ai_quotes_app.data.QuotesViewModel
import com.example.ai_quotes_app.data.SortType
import com.example.ai_quotes_app.ui.home.DarkColors   // stejné barvy jako ostatní views
import kotlin.getValue

class NotificationsFragment : Fragment() {


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
                            .padding(top = 22.dp),
                        color = DarkColors.background
                    ) {
                        SettingsScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: QuotesViewModel) {
    val state by viewModel.state.collectAsState()
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedOrder by remember { mutableStateOf("Chronological") }
    var selectedTheme by remember { mutableStateOf("Dark") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {


        Spacer(modifier = Modifier.height(24.dp))

        //TODO LANGUAGE
        SettingDropdown(
            title = "Language",
            value = selectedLanguage,
            options = listOf("English", "Czech", "German"),
            onChanged = { selectedLanguage = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ORDER BY
        SettingDropdown(
            title = "Order By",
            value = state.sortType.toString(),
            options = listOf("Chronological", "Quote", "Prompt", "Character"),
            onChanged = { newValue ->

                selectedOrder = newValue


                val sortType = when (newValue) {
                    "Chronological" -> SortType.CHRONOLOGICAL
                    "Quote" -> SortType.QUOTE
                    "Prompt" -> SortType.PROMPT
                    "Character" -> SortType.CHARACTER
                    else -> SortType.QUOTE
                }

                viewModel.onEvent(QuoteEvent.SortQuotes(sortType))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // THEME
        SettingDropdown(
            title = "Theme",
            value = selectedTheme,
            options = listOf("Dark", "Light", "System Default"),
            onChanged = { selectedTheme = it }
        )
    }
}

@Composable
fun SettingDropdown(
    title: String,
    value: String,
    options: List<String>,
    onChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = title,
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            )
        ) {
            Text(value)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onChanged(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
