package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class StatsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expenseViewModel = (requireActivity() as MainActivity).expenseViewModel

        val tvMonthSum: TextView = view.findViewById(R.id.tvMonthSum)
        val tvDayAverage: TextView = view.findViewById(R.id.tvDayAverage)

        val monthSum: Int = expenseViewModel.calculateMonthSum(LocalDate.now().month)
        val averagePerDay: Int = expenseViewModel.calculateAveragePerDay(LocalDate.now().month)
        tvMonthSum.text = formatPrintAmount(monthSum)
        tvDayAverage.text = formatPrintAmount(averagePerDay)


        val statisticsByCategoryRecyclerView =
            view.findViewById<RecyclerView>(R.id.statistics_by_categories_recycler_view)
        statisticsByCategoryRecyclerView.layoutManager = LinearLayoutManager(context)
        statisticsByCategoryRecyclerView.adapter =
            CategoryOfExpenseAdapter(CategoryOfExpense.entries, expenseViewModel)
    }


}