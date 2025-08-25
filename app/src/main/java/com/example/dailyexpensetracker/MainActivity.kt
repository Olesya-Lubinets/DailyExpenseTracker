package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.dailyexpensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val db by lazy { AppDatabase.getInstance(applicationContext) }
    private val expenseDao by lazy { db.getExpenseDao() }
    private val viewModelFactory by lazy { ExpenseViewModelFactory(expenseDao) }
    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController
    }
    val expenseViewModel: ExpenseViewModel by viewModels { viewModelFactory }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (isOpenedFromNotification(intent)) {
            navigateTo(R.id.addExpenseFragment)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isOpenedFromNotification(intent)) openAddExpenseFragment()

        setupAddButton()
        setupBottomNavigation()
    }

    private fun isOpenedFromNotification(intent: Intent?): Boolean {
        return intent?.getBooleanExtra("open_add_expense", false) == true
    }

    private fun openAddExpenseFragment() {
        navigateTo(R.id.addExpenseFragment)
    }

    private fun setupAddButton() {
        binding.btAddExpense.setOnClickListener {
            openAddExpenseFragment()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.stats_button -> navigateTo(R.id.statsFragment)
                R.id.list_button -> navigateTo(R.id.listFragment)
                R.id.settings_button -> navigateTo(R.id.settingsFragment)
            }
            true
        }
    }

    private fun navigateTo(destinationId: Int) {
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }
}
