package com.example.dailyexpensetracker


fun formatPrintAmount(amount:Int,currency: Currency):String {
    return amount.toString()+" "+ currency.title
}