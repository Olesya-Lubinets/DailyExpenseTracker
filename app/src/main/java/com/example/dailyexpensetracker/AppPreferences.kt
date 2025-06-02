package com.example.dailyexpensetracker

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private const val SETTINGS_PREFERENCES = "app-settings"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun savePreference(context: Context, key: String, value: Any) {
        val editor = getPreferences(context).edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported preference type")
        }
        editor.apply()
    }

    fun <T> getDataFromPreferences(context: Context, key: String, default: T): T {
        val preferences = getPreferences(context)
        return when (default) {
            is String -> preferences.getString(key, default) as T
            is Boolean -> preferences.getBoolean(key, default) as T
            else -> throw IllegalArgumentException("Unsupported preference type")
        }
    }
}