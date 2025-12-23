package com.example.string.logic

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.time.LocalDate

class StreakViewModel : ViewModel() {

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    private var lastDate: LocalDate? = null
    private lateinit var file: File


    fun initialize(context: Context) {
        if (::file.isInitialized) return

        file = File(context.filesDir, "streak.txt")

        if (file.exists()) {
            val lines = file.readLines()
            _streak.value = lines.firstOrNull()?.toIntOrNull() ?: 0
            lastDate = lines.getOrNull(1)?.let { LocalDate.parse(it) }
        }
    }

    fun onStreakTap() {
        val today = LocalDate.now()

        when {
            lastDate == null -> {
                _streak.value = 1
                lastDate = today
            }

            lastDate == today -> {
                // undo
                _streak.value = maxOf(0, _streak.value - 1)
                lastDate = null
            }

            lastDate == today.minusDays(1) -> {
                _streak.value += 1
                lastDate = today
            }

            else -> {
                _streak.value = 1
                lastDate = today
            }
        }

        save()
    }

    private fun save() {
        file.writeText("${_streak.value}\n${lastDate ?: ""}")
    }
}