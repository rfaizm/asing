package com.example.capstone.ui.home.history

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.data.api.response.DataPredict
import com.example.capstone.data.api.response.History
import com.example.capstone.databinding.ActivityDetailAnalyzeBinding
import com.example.capstone.databinding.ActivityHistoryBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.analyze.AnalyzeViewModel
import com.example.capstone.ui.analyze.detail.DetailAnalyzeActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
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
            intent.getParcelableExtra(DATA_DETAIL, History::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<History>(DATA_DETAIL)
        }
        val recommendation = detailData?.recommendation.toString()
        val nameFood = detailData?.predictedClassName?.capitalize().toString()
        val nameFoodsmall = detailData?.predictedClassName
        val imageUrl = detailData?.imageUrl.toString()
        val confidenceScore = detailData?.confidenceScore.toString()
        if (recommendation == "DIREKOMENDASIKAN") {
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
                            Glide.with(binding.previewImageView)
                                .load(imageUrl)
                                .into(binding.previewImageView)
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
        } else if (recommendation == "TIDAK DIREKOMENDASIKAN") {
            binding.titleTextView.text = recommendation
            binding.titleTextView.apply {
                setTextColor(Color.RED)
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
        const val DATA_DETAIL = "detail"
    }
}