package com.example.dailyexpensetracker

fun formatPrintAmount(amount:Int):String {
    return amount.toString()+" "+CurrentSettings.currentCurrency.title
}