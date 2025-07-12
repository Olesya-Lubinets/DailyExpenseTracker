package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import java.time.Month

object Calculator {
    @SuppressLint("NewApi")
    fun calculateAveragePerDay(expenses:List<Expense>,month: Month): Int { //TODO: Not formatted -> missed space after comma -> enable auto-format on commit
        val listOfExpensesEachDay = expenses.filter { it.date.month == month }
            .groupBy { it.date }
            .map { it -> it.value.sumOf { it.amount } }
        if (listOfExpensesEachDay.isEmpty()) return 0 //TODO: Place fast return on top of the method -> it is much easier to read
        return listOfExpensesEachDay.sum() / listOfExpensesEachDay.size
    }

    @SuppressLint("NewApi")
    fun calculateMonthSum(expenses:List<Expense>,month: Month): Int {
        return expenses.filter { it.date.month == month }
            .sumOf { it.amount }
    }

    @SuppressLint("NewApi")
    fun calculateMonthSumByCategory(expenses:List<Expense>, month: Month, categoryOfExpense: CategoryOfExpense): Int {
        return expenses.filter {
            it.date.month == month && it.category == categoryOfExpense }
            .sumOf { it.amount }
    }
}