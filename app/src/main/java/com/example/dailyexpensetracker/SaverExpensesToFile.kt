package com.example.dailyexpensetracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast


object SaverExpensesToFile {

    private var listOfExpenses = mutableListOf<Expense>()


    fun updateListOfExpenses(expenses: List<Expense>) {
        listOfExpenses = expenses.toMutableList()
    }

    private fun generateCSVFromData(): String {
        //TODO: I thought there should be a lib for that, but manual is ok for that case
        val sb = StringBuilder()
        sb.append("Date,Amount,Category,Description\n")
        listOfExpenses.forEach {
            sb.append("${it.date},")
            sb.append("${it.amount},")
            sb.append("${it.category.title},")
            sb.append("${it.description},")
            sb.append("\n")
        }
        return sb.toString()
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
            //todo: remove comments
            //addCategory(Intent.CATEGORY_APP_MESSAGING)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/csv"
        }
        context.startActivity(Intent.createChooser(intent, "Send via ..."))
    }

}