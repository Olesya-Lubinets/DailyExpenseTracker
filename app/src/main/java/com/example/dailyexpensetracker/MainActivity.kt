package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(applicationContext) }
    private val expenseDao by lazy { db.getExpenseDao() }
    private val viewModelFactory by lazy { ExpenseViewModelFactory(expenseDao) }

    val expenseViewModel: ExpenseViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CurrentSettings.notificationStatus =
            AppPreferences.getDataFromPreferences(this, "notifications", false)
        CurrentSettings.currentCurrency = Currency.valueOf(
            AppPreferences.getDataFromPreferences(this, "currency", "EUR")
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment


        val navController = navHostFragment.navController
        val btmAddExpense: Button = findViewById(R.id.bt_add_expense)

        btmAddExpense.setOnClickListener {
            navController.navigate(R.id.addExpenseFragment)
        }

        val navigation_bar = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        navigation_bar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.stats_button -> {
                    navController.navigate(R.id.statsFragment)
                }

                R.id.list_button -> {
                    navController.navigate(R.id.listFragment)
                }

                R.id.settings_button -> {
                    navController.navigate(R.id.settingsFragment)
                }
            }
            true
        }

    }
}
