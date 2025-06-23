package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class StatsFragment : Fragment() {
    var allExpenses:List<Expense> = emptyList()
    var currentCurrency:Currency = Currency.EUR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
        val settingsViewModel: SettingsViewModel by activityViewModels()

        val tvMonthSum: TextView = view.findViewById(R.id.tvMonthSum)
        val tvDayAverage: TextView = view.findViewById(R.id.tvDayAverage)
        val statisticsByCategoryRecyclerView =
            view.findViewById<RecyclerView>(R.id.statistics_by_categories_recycler_view)

        statisticsByCategoryRecyclerView.layoutManager = LinearLayoutManager(context)

        expenseViewModel.expensesLiveData.observe(viewLifecycleOwner) { expenses ->
            allExpenses = expenses
            updateUI(tvMonthSum,tvDayAverage, statisticsByCategoryRecyclerView)
        }
        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
            currentCurrency = currency
            updateUI(tvMonthSum,tvDayAverage, statisticsByCategoryRecyclerView)
        }
    }

    @SuppressLint("NewApi")
    fun updateUI(tvMonthSum:TextView, tvDayAverage:TextView, recyclerView: RecyclerView) {

        val monthSum = Calculator.calculateMonthSum(allExpenses,LocalDate.now().month)
        val averagePerDay = Calculator.calculateAveragePerDay(allExpenses,LocalDate.now().month)
        tvMonthSum.text = formatPrintAmount(monthSum, currentCurrency)
        tvDayAverage.text = formatPrintAmount(averagePerDay,currentCurrency)

        recyclerView.adapter =
            CategoryOfExpenseAdapter(CategoryOfExpense.entries, currentCurrency,allExpenses)
    }


}


