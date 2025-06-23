package com.example.dailyexpensetracker


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val  KEY_NOTIFICATIONS = "notifications"
private const val KEY_CURRENCY = "currency"

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    val appPreferences = AppPreferences

    private val _notificationStatus = MutableLiveData<Boolean>()
    val notificationStatus:LiveData<Boolean> = _notificationStatus

    private val _currentCurrency = MutableLiveData<Currency>()
    val currentCurrency:LiveData<Currency> = _currentCurrency

    init {
        val context = getApplication<Application>().applicationContext
        _notificationStatus.value =  appPreferences.getDataFromPreferences(context,KEY_NOTIFICATIONS,false)
        _currentCurrency.value = Currency.valueOf(appPreferences.getDataFromPreferences(context,KEY_CURRENCY,"EUR"))
    }

    fun setNotificationStatus(context: Context, enabled:Boolean) {
        _notificationStatus.value = enabled
        appPreferences.savePreference(context,KEY_NOTIFICATIONS, enabled)
    }

    fun setCurrentCurrency(context: Context, newCurrency:Currency) {
        _currentCurrency.value = newCurrency
        appPreferences.savePreference(context,KEY_CURRENCY, newCurrency.name)
    }
}