package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyexpensetracker.databinding.FragmentStatsBinding
import java.time.LocalDate

class StatsFragment : Fragment() {
    private var allExpenses: List<Expense> = emptyList()
    private var currentCurrency: Currency = Currency.EUR

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
        binding.statisticsByCategoriesRecyclerView.layoutManager = LinearLayoutManager(context)

        expenseViewModel.expensesLiveData.observe(viewLifecycleOwner) { expenses ->
            allExpenses = expenses
            updateUI(binding.statisticsByCategoriesRecyclerView)
        }
        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
            currentCurrency = currency
            updateUI(binding.statisticsByCategoriesRecyclerView)
        }
    }

    @SuppressLint("NewApi")
    private fun updateUI(recyclerView: RecyclerView) {
        val monthSum = Calculator.calculateMonthSum(allExpenses, LocalDate.now().month)
        val averagePerDay = Calculator.calculateAveragePerDay(allExpenses, LocalDate.now().month)
        binding.tvMonthSum.text = monthSum.formatWithCurrency(currentCurrency)
        binding.tvDayAverage.text = averagePerDay.formatWithCurrency(currentCurrency)

        recyclerView.adapter =
            ExpenseCategoryAdapter(ExpenseCategory.entries, currentCurrency, allExpenses)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


