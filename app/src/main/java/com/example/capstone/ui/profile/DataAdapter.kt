package com.example.capstone.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.data.model.Profile
import com.example.capstone.databinding.ItemDataBinding

class DataAdapter (private val listProfile : List<Profile>) : RecyclerView.Adapter<DataAdapter.MyViewHolder>(){
    class MyViewHolder(var binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataAdapter.MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listProfile.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val label = listProfile[position].label
        val value = listProfile[position].value

        holder.binding.label.text = label
        holder.binding.value.text = value
    }
}