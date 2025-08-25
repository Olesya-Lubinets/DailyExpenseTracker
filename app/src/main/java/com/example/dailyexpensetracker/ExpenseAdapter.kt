package com.example.dailyexpensetracker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

internal class ExpenseAdapter(private var currency: Currency) :
    ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivCategoryIcon: ImageView = view.findViewById(R.id.ivCategoryIcon)
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        private val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        private val tvDate: TextView = view.findViewById(R.id.tvDate)
        private val tvCategory: TextView = view.findViewById(R.id.tvCategory)

        fun bind(
            expenseAmountString: String,
            dateString: String,
            description: String,
            categoryTitle: String,
            pathToIcon: Int
        ) {
            tvAmount.text = expenseAmountString
            tvDate.text = dateString
            tvDescription.text = description
            tvCategory.text = categoryTitle
            Glide.with(itemView.context)
                .load(pathToIcon)
                .into(ivCategoryIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        val expenseAmountString = expense.amount.formatWithCurrency(currency)
        val expenseDateString = expense.date.toString()
        val expenseDescription = expense.description
        val expenseCategoryTitle = expense.category.title
        val pathToIcon = expense.category.icon

        holder.bind(
            expenseAmountString,
            expenseDateString,
            expenseDescription,
            expenseCategoryTitle,
            pathToIcon
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCurrency(newCurrency: Currency) {
        currency = newCurrency
        notifyDataSetChanged()
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return (oldItem.date == newItem.date && oldItem.amount == newItem.amount && oldItem.description == newItem.description
                    && oldItem.category == newItem.category)
        }
    }
}


