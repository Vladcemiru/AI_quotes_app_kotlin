package com.example.ai_quotes_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.ai_quotes_app.MainActivity
import com.example.ai_quotes_app.data.QuoteEvent
import com.example.ai_quotes_app.data.QuotesDatabase
import com.example.ai_quotes_app.data.QuotesState
import com.example.ai_quotes_app.data.QuotesViewModel

val DarkColors = darkColorScheme(
    background = Color(0xFF1B263B),
    onBackground = Color.White
)

class HomeFragment : Fragment() {

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
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            MaterialTheme(colorScheme = DarkColors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(viewModel)
                }
            }
        }
        return composeView
    }
}

@Composable
fun HomeScreen(viewModel: QuotesViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
               // .background(Color.White)
        )

        BottomSection(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
                //.background(Color.White)
            state = state,
            onEvent = viewModel::onEvent

        )
    }
}

@Composable
fun BottomSection(modifier: Modifier = Modifier, state: QuotesState, onEvent: (QuoteEvent) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderImages()

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.quote,
            onValueChange = { onEvent(QuoteEvent.SetQuote(it)) },
            label = { Text("Zadej text") },
            modifier = Modifier.fillMaxWidth(0.9f).height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        GenerateButton(state = state, onEvent = onEvent)
    }
}

@Composable
fun HeaderImages() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Levý obrázek",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Levý obrázek",
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_camera),
                contentDescription = "Pravý obrázek",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Pravý obrázek",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun GenerateButton(
    state: QuotesState,
    onEvent: (QuoteEvent) -> Unit
) {
    Button(
        onClick = {
                  onEvent(QuoteEvent.InsertQuote)
                  },
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF90CAF9),
            contentColor = Color.Black
        )
    ) {
        Text("Generate")
    }
}
