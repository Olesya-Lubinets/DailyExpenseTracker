package com.example.dailyexpensetracker

//TODO: Formatting
//TODO: Probably forth creating a separate class that will incapsulate amount and currency as not a transaction but as a expenses
// and this method could be placed there
fun formatPrintAmount(amount:Int,currency: Currency):String {
    return amount.toString()+" "+ currency.title
}