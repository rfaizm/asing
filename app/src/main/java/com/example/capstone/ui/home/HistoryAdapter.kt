package com.example.capstone.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.data.local.entity.AnalyzeHistory
import com.example.capstone.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<AnalyzeHistory, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: AnalyzeHistory) {
            binding.tvTipsTitle.text = history.analyzeResult
            binding.tvDescription.text = history.nutrition

            if (history.imageUri.isNotEmpty()) {
                val imageUri = Uri.parse(history.imageUri)
                binding.ivTips.setImageURI(imageUri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<AnalyzeHistory>() {
        override fun areItemsTheSame(oldItem: AnalyzeHistory, newItem: AnalyzeHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AnalyzeHistory, newItem: AnalyzeHistory): Boolean {
            return oldItem == newItem
        }
    }
}