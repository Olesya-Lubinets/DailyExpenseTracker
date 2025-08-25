package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import java.time.Month

object Calculator {
    @SuppressLint("NewApi")
    fun calculateAveragePerDay(expenses: List<Expense>, month: Month): Int {
        val listOfExpensesInMonth = expenses.filter { it.date.month == month }
        if (listOfExpensesInMonth.isEmpty()) return 0
        val listOfExpensesEachDay = listOfExpensesInMonth
            .groupBy { it.date }
            .map { it -> it.value.sumOf { it.amount } }
        return listOfExpensesEachDay.sum() / listOfExpensesEachDay.size
    }

    @SuppressLint("NewApi")
    fun calculateMonthSum(expenses: List<Expense>, month: Month): Int {
        return expenses.filter { it.date.month == month }
            .sumOf { it.amount }
    }

    @SuppressLint("NewApi")
    fun calculateMonthSumByCategory(
        expenses: List<Expense>,
        month: Month,
        expenseCategory: ExpenseCategory
    ): Int {
        return expenses.filter {
            it.date.month == month && it.category == expenseCategory
        }.sumOf { it.amount }
    }
}