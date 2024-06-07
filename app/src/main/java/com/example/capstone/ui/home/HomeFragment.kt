package com.example.capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.capstone.data.local.room.AnalyzeDatabase
import com.example.capstone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var database:AnalyzeDatabase
    private lateinit var adapter:HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var progressCircularBar : Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }

        historyViewModel.historyList.observe(viewLifecycleOwner, { historyList ->
            adapter.submitList(historyList)
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Room.databaseBuilder(requireContext(), AnalyzeDatabase::class.java, "history_database").build()

        progressCircularBar = 150f
        binding.circularProgressBar.apply {
            setProgressWithAnimation(progressCircularBar, 1000)

            // Set Progress Max
            progressMax = 300f
        }
        binding.numberCalories.text = "${progressCircularBar.toInt()}/300"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}