package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dailyexpensetracker.databinding.FragmentAddExpenseBinding


import java.time.LocalDate

class AddExpenseFragment : Fragment() {

    private var selectedDate: LocalDate? = null

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()
        setupExpenseSpinner()
        setupSaveButton()
        setupCancelButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NewApi")
    private fun setupDatePicker() {
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            binding.etDate.setText("$selectedDate")
        }

        val today = LocalDate.now()
        binding.imageCalendar.setOnClickListener {
            val date = selectedDate ?: today
            showDatePickerDialog(datePicker, date)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog(
        datePicker: DatePickerDialog.OnDateSetListener,
        date: LocalDate
    ) {
        DatePickerDialog(
            requireContext(),
            datePicker,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()
    }

    private fun setupExpenseSpinner() {
        val spinnerAdapter = setupSpinnerAdapter()
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        setupSpinnerCategorySelectionListener()
    }

    private fun setupSpinnerAdapter(): ArrayAdapter<ExpenseCategory?> {
        val spinnerAdapter = object : ArrayAdapter<ExpenseCategory?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + ExpenseCategory.entries
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createCustomView(
                    position, convertView, parent, android.R.layout.simple_spinner_dropdown_item
                )
            }

            private fun createCustomView(
                position: Int, convertView: View?, parent: ViewGroup, layoutId: Int
            ): View {
                val itemView =
                    convertView ?: LayoutInflater.from(requireContext()).inflate(layoutId, parent, false)
                val textView = itemView.findViewById<TextView>(android.R.id.text1)
                val item = getItem(position)
                textView?.text = item?.title ?: getString(R.string.select_category)
                return itemView
            }
        }
        return spinnerAdapter
    }

    private fun setupSpinnerCategorySelectionListener() {
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long ) {
                val selectedCategory =  binding.spinnerCategory.selectedItem as? ExpenseCategory
                val icon = selectedCategory?.icon ?: R.drawable.default_category
                showIconOfCategory(icon, binding.ivCategoryIconPreview)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showIconOfCategory(iconPath: Int, imageView: ImageView) {
        Glide.with(requireContext())
            .load(iconPath)
            .into(imageView)
    }

    private fun setupCancelButton() {
        binding.btnCancel.setOnClickListener {
            if (!checkIfAnyDataInserted()) {
                findNavController().navigateUp()
                return@setOnClickListener
            }
            showCancelDialog()
        }
    }

    private fun checkIfAnyDataInserted()
            : Boolean {
        val currentDescription = binding.etDescription.text.toString()
        val currentDateText = binding.etDate.text.toString()
        val currentAmount = binding.etAmount.text.toString()
        return (currentAmount.isNotBlank() || currentDateText.isNotBlank() || currentDescription.isNotBlank())
    }

    private fun showCancelDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sure))
            .setMessage(getString(R.string.all_entered_data_will_be_missed))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun setupSaveButton() {
        binding.btnSaveExpense.setOnClickListener {
            val currentDate = checkDate(selectedDate) ?: return@setOnClickListener
            val currentDescription = checkDescription() ?: return@setOnClickListener
            val currentAmount = checkAmount() ?: return@setOnClickListener
            val currentCategory = checkCategory() ?: return@setOnClickListener

            val currentExpense =
                buildExpenseToSave(currentDescription, currentAmount, currentDate, currentCategory)
            saveExpense(currentExpense)
        }
    }

    private fun checkDate(currentDate: LocalDate?): LocalDate? {
        if (currentDate == null)  binding.etDate.error = getString(R.string.pick_date)
        return currentDate
    }

    private fun checkDescription(): String? {
        val currentDescription: String =  binding.etDescription.text.toString()
        if (currentDescription.isBlank()) {
            binding.etDescription.error = getString(R.string.enter_description)
            return null
        }
        return currentDescription
    }

    private fun checkAmount(): Int? {
        val currentAmount =  binding.etAmount.text.toString().toIntOrNull()
        if (currentAmount == null)  binding.etAmount.error = getString(R.string.enter_valid_amount)
        return currentAmount
    }

    private fun checkCategory(): ExpenseCategory? {
        if ( binding.spinnerCategory.selectedItem == null) {
            Toast.makeText(requireContext(),
                getString(R.string.select_category), Toast.LENGTH_SHORT).show()
        }
        return  binding.spinnerCategory.selectedItem as? ExpenseCategory
    }

    private fun buildExpenseToSave(
        description: String,
        amount: Int,
        currentDate: LocalDate,
        category: ExpenseCategory
    ): Expense {
        return Expense(amount, description, currentDate, category)
    }

    private fun saveExpense(expense: Expense) {
        val expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
        expenseViewModel.addExpense(expense)
        Toast.makeText(requireContext(),
            getString(R.string.expense_successfully_added), Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
}







