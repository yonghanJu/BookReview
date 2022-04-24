package com.jyh.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jyh.bookreview.databinding.ItemHistoryBinding
import com.jyh.bookreview.model.History

class HistoryAdapter(val historyDeleteCLikedListener: (String) -> Unit):ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil){

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(historyModel:History){
            binding.historyTextView.text = historyModel.keyword

            binding.historyDeleteButton.setOnClickListener{
                historyDeleteCLikedListener(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}