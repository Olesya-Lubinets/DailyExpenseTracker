package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.LocalDate

//TODO: No usages
class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @SuppressLint("NewApi")
    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString)

    @TypeConverter
    fun fromCategory(category: CategoryOfExpense): String = category.name

    @TypeConverter
    fun toCategory(name: String): CategoryOfExpense = CategoryOfExpense.valueOf(name)
}