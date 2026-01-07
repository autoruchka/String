package com.example.string.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.time.LocalDate

class StreakViewModel : ViewModel() {

    private val fileName = "streak_log.txt"
    private val directoryName = "data"

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    private val _lastDate = MutableStateFlow<LocalDate?>(null)
    val lastDate: StateFlow<LocalDate?> = _lastDate

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private fun getFile(context: Context): File {
        val dir = File(context.filesDir, directoryName)
        if (!dir.exists()) dir.mkdirs()
        return File(dir, fileName)
    }

    /** Call once from HomeScreen */
    fun load(context: Context) {
        try {
            val file = getFile(context)
            if (!file.exists()) return

            val lines = file.readLines()
            if (lines.size < 2) return

            _streak.value = lines[0].toInt()
            _lastDate.value = LocalDate.parse(lines[1])

        } catch (e: Exception) {
            _error.value = "Failed to load streak"
            Log.e("StreakVM", "load failed", e)
        }
    }

    fun onDoubleTap(context: Context) {
        val today = LocalDate.now()
        val last = _lastDate.value

        when {
            last == today -> {
                // undo
                _streak.value = (_streak.value - 1).coerceAtLeast(0)
                _lastDate.value = today.minusDays(1)
            }

            last == today.minusDays(1) -> {
                _streak.value += 1
                _lastDate.value = today
            }

            else -> {
                _streak.value = 1
                _lastDate.value = today
            }
        }

        save(context)
    }

    private fun save(context: Context) {
        try {
            val file = getFile(context)
            file.writeText("${_streak.value}\n${_lastDate.value}")
        } catch (e: Exception) {
            _error.value = "Failed to save streak"
        }
    }

    fun dismissError() {
        _error.value = null
    }
}
