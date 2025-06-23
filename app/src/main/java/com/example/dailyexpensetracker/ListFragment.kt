package com.example.dailyexpensetracker

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.expenses_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
        val settingsViewModel: SettingsViewModel by activityViewModels()

       var currentCurrency:Currency = Currency.EUR
        val adapter = ExpenseAdapter(currentCurrency)
        recyclerView.adapter = adapter


        expenseViewModel.expensesLiveData.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
        }
        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
            currentCurrency = currency
            adapter.updateCurrency(currency)
        }

    }
}