package com.example.capstone.ui.tips.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.capstone.data.ResultState
import com.example.capstone.data.api.response.Data
import com.example.capstone.databinding.ActivityDetailTipsBinding
import com.example.capstone.ui.ViewModelFactory

class DetailTipsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTipsBinding
    private val viewModel: DetailTipsViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tipId = intent.getStringExtra("TIP_ID") ?: ""

        viewModel.detailTips.observe(this, Observer { result ->
            when (result) {
                is ResultState.Success -> {
                    displayTipDetails(result.data)
                }
                is ResultState.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Loading -> {
                    // Tampilkan indikator loading jika diperlukan
                }
            }
        })

        if (tipId.isNotEmpty()) {
            viewModel.getDetailTips(tipId)
        } else {
            Toast.makeText(this, "ID tip tidak tersedia", Toast.LENGTH_SHORT).show()
        }

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun displayTipDetails(tipDetails: Data?) {
        tipDetails?.let {
            with(binding) {
                tvTipsTitle.text = it.title
                tvDescription.text = it.description
                it.image?.let { imageUrl ->
                    Glide.with(this@DetailTipsActivity).load(imageUrl).into(ivTips)
                }
            }
        } ?: run {
            Log.e("DetailTipsActivity", "Tip details are null.")
        }
    }
}