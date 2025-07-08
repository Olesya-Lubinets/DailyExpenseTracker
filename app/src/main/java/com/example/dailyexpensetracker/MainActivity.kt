package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
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

    val settingsViewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        if (intent?.getBooleanExtra("open_add_expense", false) == true) {
            navController.navigate(R.id.addExpenseFragment)
        }
    }



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val btmAddExpense: ImageButton = findViewById(R.id.bt_add_expense)

        if (intent?.getBooleanExtra("open_add_expense", false) == true) {
            navController.navigate(R.id.addExpenseFragment)
        }
        Log.d("MainActivity", "Intent extras: ${intent?.extras}")
        Log.d("MainActivity", "open_add_expense = ${intent?.getBooleanExtra("open_add_expense", false)}")

        btmAddExpense.setOnClickListener {
            navController.navigate(R.id.addExpenseFragment)
        }

        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        bottomNavigationBar.setOnItemSelectedListener {
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
