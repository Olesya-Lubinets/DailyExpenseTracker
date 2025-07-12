package com.example.dailyexpensetracker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

//TODO: internal
class ExpenseAdapter(private var currency: Currency) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCategoryIcon: ImageView = view.findViewById(R.id.ivCategoryIcon)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.tvAmount.text = formatPrintAmount(expense.amount,currency)
        holder.tvDate.text = expense.date.toString()
        holder.tvDescription.text = expense.description
        holder.tvCategory.text = expense.category.title
        Glide.with(holder.itemView.context)
            .load(expense.category.icon)
            .into(holder.ivCategoryIcon)
    }

    fun updateCurrency(newCurrency: Currency) {
        currency = newCurrency
        //TODO: Last resort, is there something more specific?
        notifyDataSetChanged()
    }


    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        //TODO: It seems that it compares ID here too (because Expense is data class), if it is intended then ok, if not better to fix
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}


