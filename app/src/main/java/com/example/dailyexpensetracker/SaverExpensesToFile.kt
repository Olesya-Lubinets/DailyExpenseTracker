package com.example.dailyexpensetracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

object SaverExpensesToFile {

    private var listOfExpenses = mutableListOf<Expense>()

    fun updateListOfExpenses(expenses: List<Expense>) {
        listOfExpenses = expenses.toMutableList()
    }

    private fun generateCSVFromData(): String {
        return csvWriter().writeAllAsString(
            listOf(
                listOf("Date", "Amount", "Category", "Description")
            ) + listOfExpenses.map {
                listOf(it.date.toString(), it.amount.toString(), it.category.title, it.description)
            }
        )
    }

    fun saveDataToCSV(context: Context, uri: Uri) {
        val dataInString: String = generateCSVFromData()

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(dataInString.toByteArray())
            Toast.makeText(context, "Expenses saved to CSV", Toast.LENGTH_LONG).show()
        } ?: Toast.makeText(context, "Expenses NOT saved to CSV", Toast.LENGTH_LONG).show()
    }

    fun shareFile(context: Context, pathToFile: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, pathToFile)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "text/csv"
        }
        context.startActivity(Intent.createChooser(intent, "Send via ..."))
    }
}