package com.example.dailyexpensetracker

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyexpensetracker.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var expenseViewModel: ExpenseViewModel
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private val saveToStorageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            result.data?.data?.let { uri ->
                SaverExpensesToFile.saveDataToCSV(requireContext(), uri)
                SaverExpensesToFile.shareFile(requireContext(), uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = ExpenseAdapter(Currency.EUR)
        binding.expensesRecyclerView.adapter = adapter

        setupObservers(adapter)

        binding.exportToFileButton.setOnClickListener {
            exportExpensesToFile()
        }
    }

    private fun setupObservers(adapter: ExpenseAdapter) {
        expenseViewModel.expensesLiveData.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
            SaverExpensesToFile.updateListOfExpenses(expenses)
        }
        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { currency ->
            adapter.updateCurrency(currency)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun exportExpensesToFile() {
        val intent = Intent().apply {
            action = Intent.ACTION_CREATE_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, "expenses.csv")
            type = "text/csv"
        }
        saveToStorageLauncher.launch(intent)
    }
}


