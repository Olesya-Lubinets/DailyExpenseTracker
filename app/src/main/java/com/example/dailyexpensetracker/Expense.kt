package com.example.dailyexpensetracker


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Expenses")
data class Expense(
    val amount: Int, val description: String,
    val date: LocalDate, val category: CategoryOfExpense,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)








