package com.example.capstone.ui.tips

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.databinding.FragmentNotificationsBinding
import com.example.capstone.ui.register.activity.RegisterActivity
import com.example.capstone.ui.tips.detail.DetailTipsActivity

class TipsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tipsViewModel =
            ViewModelProvider(this).get(TipsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        tipsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.buttonExample.setOnClickListener {
            startActivity(Intent(requireActivity(), RegisterActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}