package com.example.capstone.ui.login

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.databinding.ActivityLoginBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.main.MainActivity
import com.example.capstone.ui.register.RegisterViewModel
import com.example.capstone.ui.register.activity.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding  = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonLogin.setOnClickListener {
            setupActionLogin()
        }


        binding.textButtonLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        } else {
            showAlert(
                getString(R.string.login_failed),
                getString(R.string.login_failed_data)
            ) { }
        }
    }

    private fun showAlert(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}