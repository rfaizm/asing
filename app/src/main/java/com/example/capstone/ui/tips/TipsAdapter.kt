package com.example.capstone.ui.tips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.api.response.DataItem
import com.example.capstone.databinding.ItemTipsBinding

class TipsAdapter : RecyclerView.Adapter<TipsAdapter.TipsViewHolder>() {
    private var _tipsList = mutableListOf<DataItem>()

    var tipsList: List<DataItem>
        get() = _tipsList
        set(value) {
            val diffCallback = TipsDiffCallback(_tipsList, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            _tipsList.apply {
                clear()
                addAll(value)
            }
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bind(_tipsList[position])
    }

    override fun getItemCount() = _tipsList.size

    inner class TipsViewHolder(private val binding: ItemTipsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tip: DataItem) {
            with(binding) {
                tvTipsTitle.text = tip.title
                tvDescription.text = tip.description
                tip.image?.let { imageUrl ->
                    Glide.with(ivTips.context).load(imageUrl).into(ivTips)
                }
            }
        }
    }
}

class TipsDiffCallback(private val oldList: List<DataItem>, private val newList: List<DataItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}