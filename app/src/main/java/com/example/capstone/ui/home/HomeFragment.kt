package com.example.capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.capstone.data.local.room.AnalyzeDatabase
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.databinding.FragmentHomeBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.home.history.HistoryAdapter
import com.example.capstone.utils.MarginItemDecoration


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var progressCircularBar: Float = 0F

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCircularProgressBar()

        binding.rvHistory.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin)))
        }

        setupAction()
    }

    private fun setupCircularProgressBar() {
        binding.circularProgressBar.apply {
            setProgressWithAnimation(progressCircularBar, 1000)
            progressMax = 2500f
        }
        if (progressCircularBar > 2500f) {
            binding.numberCalories.text = "2500/2500"
        } else {
            binding.numberCalories.text = "${progressCircularBar.toInt()}/2500"
        }

    }

    private fun setupAction() {
        viewModel.getCalories().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        updateProgress(result.data)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }

        viewModel.getHistory().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.rvHistory.apply {
                            adapter = HistoryAdapter(result.data)
                        }
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        if (result.error == "Error: Data is null") {
                            binding.historyBackground.visibility = View.VISIBLE
                        } else {
                            showToast(result.error)
                        }

                        showLoading(false)
                    }
                }
            }

        }
    }

    fun updateProgress(newProgress: Float) {
        progressCircularBar = newProgress
        binding.circularProgressBar.setProgressWithAnimation(progressCircularBar, 1000)
        if (progressCircularBar > 2500f) {
            binding.numberCalories.text = "2500/2500"
        } else {
            binding.numberCalories.text = "${progressCircularBar.toInt()}/2500"
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}