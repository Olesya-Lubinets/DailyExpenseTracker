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
//no extra empty lines GJ!
//TODO: ExpenseCategoryAdapter probably better name -> without of
internal class CategoryOfExpenseAdapter(
    //TODO: all members can be private
    val categories: List<CategoryOfExpense>,
    val currentCurrency: Currency,
    val expenses:List<Expense>
) :
//TODO: no indent for inner elements -> enable autoformat
    RecyclerView.Adapter<CategoryOfExpenseAdapter.CategoryOfExpenseViewHolder>() {
        //Not sure that we need to expose this class. Add internal
    internal inner class CategoryOfExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TODO: Do not expose full view when you need to change only text -> add specific methods for that and make vals private
        val imageCategory: ImageView = view.findViewById(R.id.imageCategory)
        val categoryOfExpense: TextView = view.findViewById(R.id.tvCategoryOfExpense)
        val sumOfExpenses: TextView = view.findViewById(R.id.sumForCategory)
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
        holder.categoryOfExpense.text = currentCategoryOfExpense.title
        val currentMonth = LocalDate.now().month
        val sumPerMonth =
           Calculator.calculateMonthSumByCategory(expenses, currentMonth, currentCategoryOfExpense)
        holder.sumOfExpenses.text = formatPrintAmount(sumPerMonth,currentCurrency)
        Glide.with(holder.itemView.context)
            .load(currentCategoryOfExpense.icon)
            .into(holder.imageCategory)
    }
}