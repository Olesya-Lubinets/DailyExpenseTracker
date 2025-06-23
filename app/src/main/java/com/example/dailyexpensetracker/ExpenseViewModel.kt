package com.example.dailyexpensetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


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
}