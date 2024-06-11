package com.example.capstone.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.data.ResultState
import com.example.capstone.databinding.FragmentTipsBinding
import com.example.capstone.ui.ViewModelFactory


class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<TipsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var tipsAdapter: TipsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
        viewModel.loadTips()
    }

    private fun setupRecyclerView() {
        tipsAdapter = TipsAdapter(listOf())
        binding.rvTips.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tipsAdapter
        }
    }

    private fun observeData() {
        viewModel.tipsLiveData.observe(viewLifecycleOwner) { resultState ->
            when (resultState) {
                is ResultState.Success -> {
                    tipsAdapter.updateData(resultState.data)
                }
                is ResultState.Error -> {
                    Toast.makeText(context, "Error: ${resultState.error}", Toast.LENGTH_LONG).show()
                }
                is ResultState.Loading -> {
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}