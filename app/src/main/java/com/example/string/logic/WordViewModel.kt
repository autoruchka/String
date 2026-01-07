package com.example.string.logic

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader

enum class LetterResult{
    CORRECT,
    PRESENT,
    WRONG

}

class WordViewModel : ViewModel(){

    private val _guesses = MutableStateFlow<List<String>>(emptyList())
    val guesses: StateFlow<List<String>> = _guesses
    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver
    private val _won = MutableStateFlow(false)
    val won: StateFlow<Boolean> = _won
    private val _letterStates = MutableStateFlow<Map<Char, LetterResult>>(emptyMap())
    val letterStates: StateFlow<Map<Char, LetterResult>> = _letterStates
    private var wordList: List<String> = emptyList()
    private var secretWord: String = ""
    private val _invalidWord = MutableStateFlow(false)
    val invalidWord: StateFlow<Boolean> = _invalidWord

    fun initialize(context: Context) {
        if (wordList.isNotEmpty()) return

        wordList = context.assets.open("words.txt")
            .bufferedReader()
            .use(BufferedReader::readLines)
            .map { it.trim().uppercase() }

        secretWord = wordList.random()
    }

    fun submitGuess(guess: String) {
        if (_gameOver.value) return
        if (guess.length != 5 || !(wordList.contains(guess))) return

        val upper = guess.uppercase()
        val newGuesses = _guesses.value + upper
        _guesses.value = newGuesses

        val updatedStates = _letterStates.value.toMutableMap()
        val results = evaluateGuess(upper)

        upper.forEachIndexed { index, char ->
            val result = results[index]
            val current = updatedStates[char]

            if (current == null || result.ordinal < current.ordinal) {
                updatedStates[char] = result
            }
        }

        _letterStates.value = updatedStates

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