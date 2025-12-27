package com.example.string.logic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class LetterResult{
    CORRECT,
    PRESENT,
    WRONG

}

class WordViewModel : ViewModel(){

    private val secretWord = "APPLE"   // later we can randomize

    private val _guesses = MutableStateFlow<List<String>>(emptyList())
    val guesses: StateFlow<List<String>> = _guesses

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver

    private val _won = MutableStateFlow(false)
    val won: StateFlow<Boolean> = _won

    fun submitGuess(guess: String) {
        if (_gameOver.value) return
        if (guess.length != 5) return

        val upper = guess.uppercase()
        val newGuesses = _guesses.value + upper
        _guesses.value = newGuesses

        if (upper == secretWord) {
            _won.value = true
            _gameOver.value = true
        } else if (newGuesses.size >= 6) {
            _gameOver.value = true
        }
    }

    fun evaluateGuess(guess: String): List<LetterResult> {
        return guess.mapIndexed { index, char ->
            when {
                secretWord[index] == char -> LetterResult.CORRECT
                secretWord.contains(char) -> LetterResult.PRESENT
                else -> LetterResult.WRONG
            }
        }
    }

}