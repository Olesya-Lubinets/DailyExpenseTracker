package com.example.dailyexpensetracker


import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class ListFragment : Fragment() {
    //TODO: privte
    val saveToStorageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) { // guard if
                result.data?.data?.let { uri ->
                    SaverExpensesToFile.saveDataToCSV(requireContext(),uri)
                    SaverExpensesToFile.shareFile(requireContext(),uri)
                }
            }
        }


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

        //TODO: probably separate method for observers
        val recyclerView: RecyclerView = view.findViewById(R.id.expenses_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
        val settingsViewModel: SettingsViewModel by activityViewModels()

       var currentCurrency:Currency = Currency.EUR
        val adapter = ExpenseAdapter(currentCurrency)
        recyclerView.adapter = adapter


        expenseViewModel.expensesLiveData.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
            SaverExpensesToFile.updateListOfExpenses(expenses)
        }
        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
            currentCurrency = currency
            adapter.updateCurrency(currency)
        }

        //todo: separate method
        val exportToFileButton = view.findViewById<Button>(R.id.exportToFileButton)
        exportToFileButton.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_CREATE_DOCUMENT
                addCategory(Intent.CATEGORY_OPENABLE)
                putExtra(Intent.EXTRA_TITLE,"expenses.csv")
                type = "text/csv"
            }
            saveToStorageLauncher.launch(intent)
        }

    }
//todo: extra empty lines

}

