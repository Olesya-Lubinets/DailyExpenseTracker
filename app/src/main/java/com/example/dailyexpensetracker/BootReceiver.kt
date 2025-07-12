package com.example.dailyexpensetracker

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
//TODO: remove extra empty lines



class  BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BootReceiver", "Received intent: ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val notificationsEnabled = AppPreferences.getDataFromPreferences(context, "notifications", false)
            if (notificationsEnabled) { //TODO: Guarding ifs
                if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    ReminderScheduler.scheduleDailyReminder(
                        context,
                        ReminderConfig.HOUR,
                        ReminderConfig.MIN
                    )
                }
            }
        }
    }
}
