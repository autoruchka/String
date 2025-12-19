package com.example.string.logic

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import kotlin.math.abs

class StreakViewModel : ViewModel() {

    private val _streak = MutableStateFlow(1)
    val streak = _streak.asStateFlow()

    private var lastDate: LocalDate? = null
    private var storage: LocalStreakStorage? = null

    // Must call this when HomeScreen is shown
    fun initialize(context: Context) {
        if (storage != null) return // already initialized

        storage = LocalStreakStorage(context)

        loadStreak()
    }

    private fun loadStreak() {
        val saved = storage?.readStreak()

        if (saved == null) {
            // first time user opens the app
            _streak.value = 1
            lastDate = LocalDate.now()
            storage?.writeStreak(1, LocalDate.now())
            return
        }

        val (savedStreak, savedDate) = saved
        lastDate = savedDate

        val today = LocalDate.now()
        val daysDifference = abs(savedDate.toEpochDay() - today.toEpochDay())

        _streak.value = if (daysDifference >= 2) 1 else savedStreak
    }

    fun onStreakTap() {
        val today = LocalDate.now()
        val last = lastDate ?: today
        val diff = (abs(last.toEpochDay() - today.toEpochDay())).toInt()

        when (diff) {
            0 -> { // same day → undo or increase
                _streak.value = if (_streak.value > 1) _streak.value - 1 else 1
            }
            1 -> { // next day → increase
                _streak.value = _streak.value + 1
            }
            else -> { // 2+ days → reset
                _streak.value = 1
            }
        }

        lastDate = today
        storage?.writeStreak(_streak.value, today)
    }
}