package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.LocalDate

internal class ExpenseCategoryAdapter(
    private val categories: List<ExpenseCategory>,
    private val currentCurrency: Currency,
    private val expenses: List<Expense>
) :
    RecyclerView.Adapter<ExpenseCategoryAdapter.CategoryOfExpenseViewHolder>() {
    class CategoryOfExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageCategory: ImageView = view.findViewById(R.id.imageCategory)
        private val categoryOfExpense: TextView = view.findViewById(R.id.tvCategoryOfExpense)
        private val sumOfExpenses: TextView = view.findViewById(R.id.sumForCategory)

        fun bind(categoryTitle: String, sumOfExpensesString: String, pathToIcon: Int) {
            categoryOfExpense.text = categoryTitle
            sumOfExpenses.text = sumOfExpensesString
            Glide.with(imageCategory.context)
                .load(pathToIcon)
                .into(imageCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryOfExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_of_expense, parent, false)
        return CategoryOfExpenseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: CategoryOfExpenseViewHolder, position: Int) {
        val currentCategoryOfExpense = categories[position]

        val categoryTitle = currentCategoryOfExpense.title
        val currentMonth = LocalDate.now().month
        val sumPerMonth =
            Calculator.calculateMonthSumByCategory(expenses, currentMonth, currentCategoryOfExpense)
        val sumOfExpensesString = sumPerMonth.formatWithCurrency(currentCurrency)
        val pathToIcon = currentCategoryOfExpense.icon

        holder.bind(categoryTitle, sumOfExpensesString, pathToIcon)
    }
}