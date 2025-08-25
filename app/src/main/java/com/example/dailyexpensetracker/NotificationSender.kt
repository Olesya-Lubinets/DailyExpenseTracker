package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

object NotificationSender {

    private const val CHANNEL_ID = "EverydayReminder"
    private const val CHANNEL_NAME = "Everyday reminder"
    private const val DAILY_REMINDER_NOTIFICATION_ID = 1

    @SuppressLint("NewApi")
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotificationBuilder(context: Context): NotificationCompat.Builder {
        val openAppIntent = Intent(context, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            it.putExtra("open_add_expense", true)
        }
        val openAppPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.pig_app_icon)
            .setContentTitle("Expenses reminder")
            .setContentText("Don't forget to add today's expenses")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppPendingIntent)
            .setAutoCancel(true)
        return notificationBuilder
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendReminder(context: Context) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)
        val notificationBuilder = createNotificationBuilder(context)
        notificationManager.notify(DAILY_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
        Log.d("NotificationSender", "Sending notification…")
    }
}

