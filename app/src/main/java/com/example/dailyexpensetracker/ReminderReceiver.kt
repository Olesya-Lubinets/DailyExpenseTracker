package com.example.dailyexpensetracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("ReminderReceiver", "Alarm received! Sending notification.")

        NotificationSender.sendReminder(context)

        // Schedule the next day
        //TODO: Separate method getNextDayCalendar()?
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, ReminderConfig.HOUR)
            set(Calendar.MINUTE, ReminderConfig.MIN)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        ReminderScheduler.scheduleExactReminder(context, calendar)
    }
}