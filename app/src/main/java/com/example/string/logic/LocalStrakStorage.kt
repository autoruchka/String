package com.example.string.logic

import android.content.Context
import java.io.File
import java.time.LocalDate

class LocalStreakStorage(private val context: Context) {
    private val fileName = "data/streak_log.txt"

    private fun getFile(): File =
        File(fileName)

    fun readStreak(): Pair<Int, LocalDate>? {
        val file = getFile()
        if (!file.exists()) return null

        val lines = file.readLines()
        if (lines.size < 2) return null

        val streak = lines[0].toIntOrNull() ?: return null
        val lastDate = LocalDate.parse(lines[1])

        return streak to lastDate
    }

    fun writeStreak(streak: Int, date: LocalDate) {
        val file = getFile()
        file.writeText("$streak\n$date")
    }
}