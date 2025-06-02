package com.example.dailyexpensetracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import java.time.LocalDate


class AddExpenseFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false)
    }

    // remove all this annotations
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val spinnerAdapter = object : ArrayAdapter<CategoryOfExpense?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(null) + CategoryOfExpense.entries
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createCustomView(
                    position,
                    convertView,
                    parent,
                    android.R.layout.simple_spinner_dropdown_item
                )
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return createCustomView(
                    position,
                    convertView,
                    parent,
                    android.R.layout.simple_spinner_dropdown_item
                )
            }

            private fun createCustomView(
                position: Int,
                convertView: View?,
                parent: ViewGroup,
                layoutId: Int
            ): View {
                val view =
                    convertView ?: LayoutInflater.from(context).inflate(layoutId, parent, false)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                val item = getItem(position)
                textView?.text = item?.title ?: "Select category"
                return view
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerCategory: Spinner = view.findViewById(R.id.spinnerCategory)
        spinnerCategory.adapter = spinnerAdapter

        val ivCategoryIconPreview: ImageView = view.findViewById(R.id.ivCategoryIconPreview)
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val temp = spinnerCategory.selectedItem as? CategoryOfExpense
                val icon = temp?.icon ?: R.drawable.default_category
                showIconOfCategory(icon, ivCategoryIconPreview)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val myCalendar = Calendar.getInstance()

        val etDate: EditText = view.findViewById(R.id.etDate)

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date: LocalDate = LocalDate.of(year, month + 1, dayOfMonth)
            etDate.setText("$date")
        }

        val imageCalendar: ImageView = view.findViewById(R.id.imageCalendar)
        imageCalendar.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val etDescription: EditText = view.findViewById(R.id.etDescription)
        val etAmount: EditText = view.findViewById(R.id.etAmount)

        val btnSaveExpense: Button = view.findViewById(R.id.btnSaveExpense)
        btnSaveExpense.setOnClickListener {

            val currentDescription: String = etDescription.text.toString()
            if (currentDescription.isBlank()) {
                etDescription.error = "Enter a description"
                return@setOnClickListener
            }
            if (etDate.text.isNullOrBlank()) {
                etDate.error = "Pick a date"
                return@setOnClickListener
            }
            val currentDate: LocalDate = LocalDate.of(
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH) + 1,
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )

            val currentAmount = etAmount.text.toString().toIntOrNull() ?: run {
                etAmount.error = "Enter a valid amount"
                return@setOnClickListener
            }

            if (spinnerCategory.selectedItem == null) {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val currentCategory: CategoryOfExpense =
                spinnerCategory.selectedItem as CategoryOfExpense

            val currentExpense =
                Expense(currentAmount, currentDescription, currentDate, currentCategory)


            val expenseViewModel = (requireActivity() as MainActivity).expenseViewModel
            expenseViewModel.addExpense(currentExpense)
            Toast.makeText(context, "Expense successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()

        }

        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            val currentDescription = etDescription.text.toString()
            val currentDateText = etDate.text.toString()
            val currentAmount = etAmount.text.toString()
            if (checkIfAnyDataInserted(currentAmount, currentDateText, currentDescription)) {
                showCancelDialog()
            } else findNavController().navigateUp()
        }

    }

    fun showIconOfCategory(iconPath: Int, imageView: ImageView) {
        Glide.with(requireContext())
            .load(iconPath)
            .into(imageView)
    }


    private fun showCancelDialog() {
        AlertDialog.Builder(context)
            .setTitle("Are you sure?")
            .setMessage("All entered data will be missed.")
            .setPositiveButton("Yes") { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

private fun checkIfAnyDataInserted(amount: String, dateText: String, description: String)
        : Boolean {
    return (amount.isNotBlank() || dateText.isNotBlank() || description.isNotBlank())
}

