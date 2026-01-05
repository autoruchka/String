package com.example.string.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var currentGuess by remember { mutableStateOf("") }

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
                    .background(Color(0xFFC6651A))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap to submit",
                modifier = Modifier
                    .background(Color(0xFFC6651A))
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
                style = MaterialTheme.typography.headlineMedium
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
                    color = Color.White
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
                    .background(Color(0xFFC6651A))
            )
        }
    }
}
