package com.example.ai_quotes_app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
import com.example.ai_quotes_app.R

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
                    modifier = Modifier.fillMaxSize() .padding(top = 22.dp),
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
    Box(modifier = Modifier.height(200.dp).fillMaxSize()){}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            QuoteCard(state)
        }

            BottomSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                    //.background(Color.White)
                state = state,
                onEvent = viewModel::onEvent,
                onGenerate = { tone -> viewModel.apiCall(tone) }


            )
    }
}

@Composable
fun BottomSection(modifier: Modifier = Modifier, state: QuotesState, onEvent: (QuoteEvent) -> Unit, onGenerate: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var selectedTone by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderImages(state = state, onEvent = onEvent, selectedTone, onToneSelected = { selectedTone = it })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.prompt,
            onValueChange = { onEvent(QuoteEvent.SetPrompt(it)) },
            label = { Text("Enter Your input") },
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

        GenerateButton( state = state,
                        tone = selectedTone,
                        onGenerate = { onGenerate(selectedTone) } )
    }
}


@Composable
fun HeaderImages(    state: QuotesState,
                     onEvent: (QuoteEvent) -> Unit,
                     tone: String,
                     onToneSelected: (String) -> Unit) {

    var showCharacter by remember { mutableStateOf(false) }
    var showTone by remember { mutableStateOf(false) }

    val characterNames = listOf(
            "Winston Churchill", "Mahatma Gandhi", "Oscar Wilde", "Mark Twain", "Nelson Mandela",
        "Friedrich Nietzsche", "Steve Jobs", "Erwin Rommel", "Napoleon Bonaparte", "Julius Caesar", "Theodore Roosevelt"
    )
    val characterImages = mapOf(
        "Winston Churchill" to R.drawable.churchill,
        "Mahatma Gandhi" to R.drawable.gandhi,
        "Erwin Rommel" to R.drawable.rommel,
        "Oscar Wilde" to R.drawable.wilde,
        "Mark Twain" to R.drawable.twain1,
        "Nelson Mandela" to R.drawable.mandela ,
        "Friedrich Nietzsche" to  R.drawable.nietzsche,
        "Steve Jobs" to R.drawable.jobs,
        "Erwin Rommel" to R.drawable.rommel,
        "Napoleon Bonaparte" to R.drawable.napoleon,
        "Julius Caesar" to R.drawable.cessar,
        "Theodore Roosevelt" to R.drawable.roosvelt
    )

    val tonesImages = mapOf(
        "Calm" to R.drawable.calm,
        "Angry" to R.drawable.angry,
        "Happy" to R.drawable.happy,
        "Sad" to R.drawable.sad
    )



    val toneOptions = listOf("Calm", "Angry", "Happy", "Sad")

    Box(Modifier.fillMaxWidth()) {

      //pictures row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // CHARACTER
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                val characterImageRes = characterImages[state.character] ?: R.drawable.guesss

                Image(
                    painter =  painterResource(characterImageRes),
                    contentDescription = "Character",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable {
                            showCharacter = !showCharacter
                            showTone = false
                        }
                )
                Text(text = if (state.character.isBlank()) "Choose Character" else state.character,
                    modifier = Modifier.width(160.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1)
            }

            // TONE
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                val toneImageRes = tonesImages[tone] ?: R.drawable.tone

                Image(
                    painter =  painterResource(toneImageRes),
                    contentDescription = "Tone",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable {
                            showTone= !showTone
                            showCharacter = false
                        }
                )
                Text(text = if (tone.isBlank()) "Select Tone" else tone,
                    modifier = Modifier.width(100.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1)
            }
        }

        // Character dropdown
        if (showCharacter) {
            DropdownMenuList(
                items = characterNames,
                onItemSelected = { selected->
                    onEvent(QuoteEvent.SetCharacter(selected))
                    showCharacter = false },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 110.dp)
                    .width(180.dp),
                height = 200.dp
            )
        }

        // Tone dropdown
        if (showTone) {
            DropdownMenuList(
                items = toneOptions,
                onItemSelected = { selected ->
                    onToneSelected(selected)
                    showTone = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 24.dp, top = 110.dp)
                    .width(180.dp),
                height = 160.dp
            )
        }
    }
}


@Composable
fun QuoteCard(state: QuotesState) {
    AnimatedVisibility(visible = state.quote.isNotBlank()) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF22344D)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)   // maximální výška pro dlouhé citáty
                ) {
                    item {
                        Text(
                            text = state.quote,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun DropdownMenuList(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 200.dp
) {
    Card(modifier = modifier) {

        Box {
            LazyColumn(
                modifier = Modifier
                    .height(height)
                    .padding(8.dp)
            ) {
                items(items) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable { onItemSelected(item) }
                    )
                }
            }

            // Fade top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Fade bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun GenerateButton(  state: QuotesState,
                     tone: String,
                     onGenerate: () -> Unit) {
    Button(
        onClick = {
            onGenerate()
                  },
        enabled = !state.isLoading,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF90CAF9),
            contentColor = Color.Black
        )
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color.Black
            )
        } else {
            Text("Generate")
        }
    }
}

//fun apiCall(    state: QuotesState,
//                 onEvent: (QuoteEvent) -> Unit,
//                 tone: String) {






    //when i get answer... do this
    //onEvent(QuoteEvent.InsertQuote)
//}