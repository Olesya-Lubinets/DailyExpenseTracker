package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.Month

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {
    var expensesLiveData: LiveData<List<Expense>> = expenseDao.getAllExpenses().asLiveData()

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insert(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.delete(expense)
        }
    }

    @SuppressLint("NewApi")
    fun calculateAveragePerDay(month: Month): Int {
        val listOfExpensesEachDay = expensesLiveData.value
            ?.filter { it.date.month == month }
            ?.groupBy { it.date }
            ?.map { it -> it.value.sumOf { it.amount } } ?: return 0
        if (listOfExpensesEachDay.isEmpty()) return 0
        return listOfExpensesEachDay.sum() / listOfExpensesEachDay.size
    }

    @SuppressLint("NewApi")
    fun calculateMonthSum(month: Month): Int {
        return expensesLiveData.value?.filter { it.date.month == month }
            ?.sumOf { it.amount }
            ?: 0
    }

    @SuppressLint("NewApi")
    fun calculateMonthSumByCategory(month: Month, categoryOfExpense: CategoryOfExpense): Int {
        return expensesLiveData.value
            ?.filter { it.date.month == month && it.category == categoryOfExpense }
            ?.sumOf { it.amount }
            ?: 0
    }
}