package com.example.capstone.ui.home.history

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.api.response.DataHistory
import com.example.capstone.data.api.response.History
import com.example.capstone.databinding.ItemHistoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryAdapter (private var listHistory : List<DataHistory?>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>(){

    class MyViewHolder(var binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataHistory){
            val captalize = user.history?.predictedClassName?.capitalize()
            binding.tvTipsTitle.text = captalize
            Glide.with(binding.ivTips)
                    .load(user.history?.imageUrl.toString())
                    .into(binding.ivTips)
            Log.d("HistoryAdapter", "URL: ${user.history?.imageUrl}")
            binding.tvDescription.text = user.history?.confidenceScore.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryAdapter.MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = listHistory[position]

        holder.bind(user!!)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (user.history?.recommendation.toString() == "TIDAK DIREKOMENDASIKAN") {
                showAlert(context, "TIDAK DIREKOMENDASIKAN", "Makanan anda tidak direkomendasikan") { dialog -> }

            } else if (user.history?.recommendation.toString() == "DIREKOMENDASIKAN") {
                val intent = Intent(context, HistoryActivity::class.java)
                intent.putExtra(HistoryActivity.DATA_DETAIL, user.history)
                context.startActivity(intent)
            }

        }
    }

    private fun showAlert(
        context: Context,
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            setCancelable(false)
            create()
            show()
        }
    }
}