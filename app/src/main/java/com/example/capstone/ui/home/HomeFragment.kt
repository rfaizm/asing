package com.example.capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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