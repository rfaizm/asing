package com.example.capstone.ui.profile.detail

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.databinding.ActivityProfileUpdateBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.main.MainActivity
import com.example.capstone.ui.profile.ProfileViewModel

class ProfileUpdateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileUpdateBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonSave.setOnClickListener {
            setupActionEdit()
        }

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionEdit() {
        val fullName = binding.fullnameEditText.text.toString()
        val age = binding.ageEditText.text.toString().toIntOrNull()
        val height = binding.bodyheightEditText.text.toString().toFloatOrNull()
        val weight = binding.bodyweightEditText.text.toString().toFloatOrNull()
        val circleHand = binding.circleHandEditText.text.toString().toFloatOrNull()

        if (!TextUtils.isEmpty(fullName)
            && !TextUtils.isEmpty(age.toString()) && !TextUtils.isEmpty(height.toString()) && !TextUtils.isEmpty(weight.toString())
            && !TextUtils.isEmpty(circleHand.toString())) {

            viewModel.updateProfile(fullName, height!!, weight!!, age!!, circleHand!!).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading ->{
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } else {
            showAlert(
                getString(R.string.update_gagal),
                getString(R.string.update_gagal_empty)
            ) { }
        }
    }

    private fun showAlert(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            setCancelable(false)
            create()
        }.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.green))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}