package com.example.capstone.ui.tips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.api.response.Data
import com.example.capstone.databinding.ItemTipsBinding

class TipsAdapter(private var tipsList: List<Data>) : RecyclerView.Adapter<TipsAdapter.TipsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bind(tipsList[position])
    }

    override fun getItemCount(): Int = tipsList.size

    fun updateData(newTipsList: List<Data>) {
        tipsList = newTipsList
        notifyDataSetChanged()
    }

    class TipsViewHolder(private val binding: ItemTipsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tip: Data) {
            binding.tvTipsTitle.text = tip.title
            binding.tvDescription.text = tip.description
            Glide.with(binding.root.context).load(tip.image).into(binding.ivTips)
        }
    }
}