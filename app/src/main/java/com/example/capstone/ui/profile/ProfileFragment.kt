package com.example.capstone.ui.profile

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
import com.example.capstone.data.model.Profile
import com.example.capstone.databinding.FragmentProfileBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.profile.detail.ProfileUpdateActivity

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private lateinit var profile : List<Profile>


    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()

        binding.btnLogout.setOnClickListener {
            setupActionLogout()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(activity, ProfileUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        setupAction()
    }

    private fun setupAction() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.name
            binding.tvEmail.text = user.email
            binding.ivImageUser.context.resources.getIdentifier("icon_asing", "drawable", binding.ivImageUser.context.packageName)

            profile = toList(user.age.toString(), "${user.handCircle} CM", "${user.height} CM", "${user.weight} KG")

            binding.rvData.apply {
                val layoutManager = LinearLayoutManager(requireContext())
                binding.rvData.layoutManager = layoutManager
                val listDataAdapter = DataAdapter(profile)
                binding.rvData.adapter = listDataAdapter
            }
        }
    }

    private fun setupActionLogout() {
        viewModel.logoutSession().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading ->{
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        viewModel.logout()
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun toList(age : String, circleHand : String, height : String, weight : String) : List<Profile> {
        return listOf(
            Profile(label = "Usia", value = age),
            Profile(label = "Lingkar Lengan", value = circleHand),
            Profile(label = "Tinggi Badan", value = height),
            Profile(label = "Berat Badan", value = weight)
        )
    }

}