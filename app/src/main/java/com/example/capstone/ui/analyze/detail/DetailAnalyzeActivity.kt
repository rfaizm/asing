package com.example.capstone.ui.analyze.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.data.api.response.DataPredict
import com.example.capstone.data.local.entity.AnalyzeHistory
import com.example.capstone.data.local.room.AnalyzeDatabase
import com.example.capstone.databinding.ActivityDetailAnalyzeBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.analyze.AnalyzeViewModel
import com.example.capstone.ui.home.history.HistoryViewModel
import com.example.capstone.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DetailAnalyzeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailAnalyzeBinding
    private lateinit var database: AnalyzeDatabase
    private lateinit var historyViewModel: HistoryViewModel

    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(this)
    }

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

        database = AnalyzeDatabase.getDatabase(applicationContext)
        historyViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[HistoryViewModel::class.java]

        val analyzeResult = intent.getStringExtra("PREDICTION_RESULT")
        val nutrition = intent.getStringExtra("NUTRITION")
        val confidenceScore = intent.getStringExtra("CONFIDENCE_SCORE") ?: "0"
        val imageUriString = intent.getStringExtra("IMAGE_URI")

        if (analyzeResult != null && imageUriString != null) {
            if (nutrition != null) {
                saveAnalyzeToDatabase(imageUriString, analyzeResult, nutrition, confidenceScore)
            }
        } else {
            showToast("Informasi tidak lengkap untuk ditampilkan atau disimpan.")
        }

        val imageUri = Uri.parse(imageUriString)
        if (imageUri != null) {
        } else {
            showToast("Gagal memuat gambar.")
        }

        binding.ibBack.setOnClickListener {
            finish()
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
        val nameFoodsmall = detailData?.predictedClassName
        val recommendation = detailData?.recommendation.toString()
        val confidenceScore = detailData?.confidenceScore.toString() // Ambil string asli dengan tanda persen
        imageUri?.let {
            binding.previewImageView.setImageURI(it)
        }

        viewModel.getNutrition(nameFoodsmall!!).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        val calories = result.data.data?.calories!!.toFloat()
                        val protein = result.data.data.proteins
                        val fat = result.data.data.fat
                        Log.d("DetailAnalyzeActivity", "calories: $calories")
                        uploadProgressCalories(calories)
                        binding.titleTextView.text = recommendation
                        binding.descResultTextView.text = nameFood
                        binding.descScoreTextView.text = confidenceScore
                        binding.descNutritionTextView.text = "Kalori: ${calories} kkal"
                        binding.descNutritionProteinTextView.text = "Protein: ${protein} gram"
                        binding.descNutritionFatTextView.text = "Fat: ${fat} gram"
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

    private fun uploadProgressCalories(calories : Float) {
        viewModel.uploadCalories(calories).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        Log.d("DetailAnalyzeActivity", "success upload")
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

    private fun saveAnalyzeToDatabase(imageUri: String, analyzeResult: String, nutrition: String, confidenceScore: String) {
        val history = AnalyzeHistory(
            imageUri = imageUri,
            analyzeResult = analyzeResult,
            nutrition = nutrition,
            confidenceScore = confidenceScore
        )
        historyViewModel.insertAnalyzeHistory(history)
        Log.d("DetailAnalyzeActivity", "Analisis berhasil disimpan: $history")
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