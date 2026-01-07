package com.example.string.logic

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException
import java.time.LocalDate

class LocalStreakStorage(private val context: Context) {
    private val fileName = "streak_log.txt"
    private val directoryName = "data"

    companion object {
        private const val TAG = "LocalStreakStorage"
    }

    /**
     * Gets the file in app's internal storage
     * Path: /data/data/com.example.string/files/data/streak_log.txt
     */
    private fun getFile(): File {
        // Create directory in app's internal storage
        val directory = File(context.filesDir, directoryName)
        if (!directory.exists()) {
            val created = directory.mkdirs()
            if (!created) {
                Log.w(TAG, "Failed to create directory: ${directory.absolutePath}")
            }
        }
        return File(directory, fileName)
    }

    /**
     * Reads the streak data from storage
     * @return Pair of (streak count, last date) or null if file doesn't exist or is invalid
     */
    fun readStreak(): Pair<Int, LocalDate>? {
        return try {
            val file = getFile()
            if (!file.exists()) {
                Log.d(TAG, "Streak file doesn't exist yet")
                return null
            }

            val lines = file.readLines()
            if (lines.size < 2) {
                Log.w(TAG, "Invalid streak file format: insufficient lines")
                return null
            }

            val streak = lines[0].toIntOrNull()
            if (streak == null) {
                Log.w(TAG, "Invalid streak value: '${lines[0]}'")
                return null
            }

            val lastDate = try {
                LocalDate.parse(lines[1])
            } catch (e: Exception) {
                Log.w(TAG, "Invalid date format: '${lines[1]}'", e)
                return null
            }

            Log.d(TAG, "Successfully read streak: $streak, date: $lastDate")
            streak to lastDate

        } catch (e: IOException) {
            Log.e(TAG, "Error reading streak file", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error reading streak", e)
            null
        }
    }

    /**
     * Writes the streak data to storage (synchronous)
     * @param streak The streak count to save
     * @param date The date to save
     * @return true if successful, false otherwise
     */
    fun writeStreak(streak: Int, date: LocalDate): Boolean {
        return try {
            val file = getFile()
            val content = "$streak\n$date"

            file.writeText(content)

            // Verify write was successful
            if (file.exists() && file.readText() == content) {
                Log.d(TAG, "Successfully wrote streak: $streak, date: $date")
                true
            } else {
                Log.e(TAG, "Write verification failed")
                false
            }

        } catch (e: IOException) {
            Log.e(TAG, "IO error writing streak file", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error writing streak", e)
            false
        }
    }

    /**
     * Clears the streak data
     * @return true if file was deleted successfully
     */
    fun clearStreak(): Boolean {
        return try {
            val file = getFile()
            if (file.exists()) {
                val deleted = file.delete()
                if (deleted) {
                    Log.d(TAG, "Streak file deleted successfully")
                } else {
                    Log.w(TAG, "Failed to delete streak file")
                }
                deleted
            } else {
                Log.d(TAG, "Streak file doesn't exist, nothing to delete")
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting streak file", e)
            false
        }
    }

    /**
     * Gets the absolute path for debugging purposes
     */
    fun getFilePath(): String {
        return getFile().absolutePath
    }
}