package com.example.capstone.ui.tips

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.data.ResultState
import com.example.capstone.data.api.response.DataItem
import com.example.capstone.databinding.FragmentTipsBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.tips.detail.DetailTipsActivity


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
    }

    private fun setupRecyclerView() {
        tipsAdapter = TipsAdapter(object : TipsAdapter.OnItemClickListener {
            override fun onItemClick(tip: DataItem) {
                val intent = Intent(activity, DetailTipsActivity::class.java)
                intent.putExtra("TIP_ID", tip.id)
                startActivity(intent)
            }
        })
        binding.rvTips.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tipsAdapter
        }
    }

    private fun observeData() {
        viewModel.tipsLiveData.observe(viewLifecycleOwner) { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    resultState.data?.let { dataList ->
                        tipsAdapter.tipsList = dataList.filterNotNull()
                    } ?: run {
                        showToast("No tips available")
                    }
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showToast(resultState.error)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}