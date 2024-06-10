package com.example.capstone.ui.analyze.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.data.api.response.DataPredict
import com.example.capstone.databinding.ActivityDetailAnalyzeBinding
import com.example.capstone.ui.SharedViewModel
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.analyze.AnalyzeViewModel
import com.example.capstone.ui.home.HomeFragment

class DetailAnalyzeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailAnalyzeBinding

    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val sharedViewModel by viewModels<SharedViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()

    }

    private fun setupAction() {
        val detailData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DATA_DETAIL, DataPredict::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DataPredict>(DATA_DETAIL)
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val nameFood = detailData?.predictedClassName?.capitalize().toString()
        imageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
        binding.descResultTextView.text = nameFood
        binding.titleTextView.text = detailData?.recommendation.toString()
        binding.descScoreTextView.text = detailData?.confidenceScore.toString()
        viewModel.getNutrition(nameFood).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        val calories = result.data.data?.calories!!.toFloat()
                        binding.descNutritionTextView.text = calories.toString()
                        sharedViewModel.setCalories(calories)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val DATA_DETAIL = "detail"
    }
}