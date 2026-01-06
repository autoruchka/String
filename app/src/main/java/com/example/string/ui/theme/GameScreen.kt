package com.example.string.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.TransferableContent.Source.Companion.Keyboard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.string.logic.LetterResult
import com.example.string.logic.WordViewModel


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: WordViewModel = viewModel()
) {
    val guesses by viewModel.guesses.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()
    val won by viewModel.won.collectAsState()
    val letterStates by viewModel.letterStates.collectAsState()
    var currentGuess by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.initialize(context) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF3D1611))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        guesses.forEach { guess ->
            GuessRow(guess, viewModel.evaluateGuess(guess))
        }

        repeat(6 - guesses.size) {
            EmptyRow()
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!gameOver) {
            BasicTextField(
                value = currentGuess,
                onValueChange = {
                    if (it.length <= 5) currentGuess = it.uppercase()
                },
                textStyle = TextStyle(fontSize = MaterialTheme.typography.headlineMedium.fontSize),
                modifier = Modifier
                    .background(Color(0xFF5E271C))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Tap to submit",
                modifier = Modifier
                    .background(Color(0xFF5E271C))
                    .padding(8.dp)
                    .clickable {
                        viewModel.submitGuess(currentGuess)
                        currentGuess = ""
                    },
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(
                text = if (won) "You won!" else "You lost :(",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .background(Color(0xFF5E271C))
            )
        }


        val invalidWord by viewModel.invalidWord.collectAsState()

        Keyboard(
            letterStates = letterStates,
            onLetter = {
                if (currentGuess.length < 5 && !gameOver)
                    currentGuess += it
            },
            onDelete = {
                currentGuess = currentGuess.dropLast(1)
            },
            onEnter = {
                viewModel.submitGuess(currentGuess)
                currentGuess = ""
            }
        )

        if (invalidWord) {
            Text(
                text = "Not a valid word",
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge
            )
        }


    }
}

@Composable
fun GuessRow(guess: String, results: List<LetterResult>) {
    Row {
        guess.forEachIndexed { index, char ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .padding(4.dp)
                    .background(
                        when (results[index]) {
                            LetterResult.CORRECT -> Color(0xFFFFFBA9)
                            LetterResult.PRESENT -> Color(0xFFFF7700)
                            LetterResult.WRONG -> Color(0xFF3D1611)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = (
                            when (results[index]) {
                                LetterResult.CORRECT -> Color(0xFF3D1611)
                                LetterResult.PRESENT -> Color(0xFF3D1611)
                                LetterResult.WRONG -> Color(0xFFFFFFFF)
                            }
                            )
                )
            }
        }
    }
}

@Composable
fun EmptyRow() {
    Row {
        repeat(5) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .padding(4.dp)
                    .background(Color(0xFF5E271C))
            )
        }
    }
}


@Composable
fun Keyboard(
    letterStates: Map<Char, LetterResult>,
    onLetter: (Char) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        KeyboardRow("QWERTYUIOP", letterStates, onLetter)
        KeyboardRow("ASDFGHJKL", letterStates, onLetter)
        Row {
            Key("ENTER", Color(0xFF5E271C)) { onEnter() }
            KeyboardRow("ZXCVBNM", letterStates, onLetter)
            Key("⌫", Color(0xFF5E271C)) { onDelete() }
        }
    }
}

@Composable
fun KeyboardRow(
    letters: String,
    letterStates: Map<Char, LetterResult>,
    onLetter: (Char) -> Unit
) {
    Row {
        letters.forEach { char ->
            val color = when (letterStates[char]) {
                LetterResult.CORRECT -> Color(0xFFFFFBA9)
                LetterResult.PRESENT -> Color(0xFFFF7700)
                LetterResult.WRONG -> Color(0xFF3D1611)
                null -> Color(0xFF5E271C)
            }
            Key(char.toString(), color) { onLetter(char) }
        }
    }
}

@Composable
fun Key(label: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(48.dp)
            .width(if (label.length > 1) 64.dp else 36.dp)
            .background(color, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
