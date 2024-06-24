package com.example.capstone.ui.register.fragment

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.databinding.FragmentRegisterDataBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.login.LoginActivity
import com.example.capstone.ui.register.RegisterViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class RegisterDataFragment : Fragment() {

    private lateinit var binding : FragmentRegisterDataBinding
    private var email : String? = null
    private var password : String? = null

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterDataBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawProgressIndicator()

        if (arguments != null) {
            email = arguments?.getString(EXTRA_EMAIL)
            password = arguments?.getString(EXTRA_PASSWORD)
        }

        binding.buttonSave.setOnClickListener {
            setupActionRegister()
        }
    }

    private fun setupActionRegister() {
        val fullName = binding.fullnameEditText.text.toString()
        val age = binding.ageEditText.text.toString().toIntOrNull()
        val height = binding.bodyheightEditText.text.toString().toFloatOrNull()
        val weight = binding.bodyweightEditText.text.toString().toFloatOrNull()
        val circleHand = binding.circleHandEditText.text.toString().toFloatOrNull()

        if (!TextUtils.isEmpty(fullName)
            && !TextUtils.isEmpty(age.toString()) && !TextUtils.isEmpty(height.toString()) && !TextUtils.isEmpty(weight.toString())
            && !TextUtils.isEmpty(circleHand.toString()))
        {
            viewModel.register(email!!, password!!, fullName, height!!, weight!!, age!!, circleHand!!).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading ->{
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            MaterialAlertDialogBuilder(requireContext()).apply {
                                setTitle("Yeah!")
                                setMessage("Akun dengan berhasil dibuat")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
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
            }
        } else {
            showAlert(
                getString(R.string.register_gagal),
                getString(R.string.register_gagal_1)
            ) { }
        }
    }

    private fun showAlert(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        MaterialAlertDialogBuilder(requireContext()).apply {
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun drawProgressIndicator() {
        // Create a bitmap and a canvas to draw on it
        val mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(mBitmap)
        val mPaint = Paint()

        val circleRadius = 50f
        val circleSpacing = 300f
        val startX = 150f
        val startY = 100f

        // Draw the first circle (completed)
        mPaint.color = Color.parseColor("#229552")
        mCanvas.drawCircle(startX, startY, circleRadius, mPaint)

        // Draw check mark in the first circle
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 8f
        mPaint.style = Paint.Style.STROKE
        mCanvas.drawLine(startX - 20, startY, startX, startY + 20, mPaint)
        mCanvas.drawLine(startX, startY + 20, startX + 30, startY - 10, mPaint)

        // Draw the line between first and second circles
        mPaint.color = Color.parseColor("#CCCCCC")
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mCanvas.drawLine(startX + circleRadius, startY, startX + circleRadius + circleSpacing, startY, mPaint)

        // Draw the second circle (completed, similar to the first circle)
        val secondCircleCenterX = startX + circleRadius + circleSpacing
        mPaint.color = Color.parseColor("#229552")
        mPaint.style = Paint.Style.FILL
        mCanvas.drawCircle(secondCircleCenterX, startY, circleRadius, mPaint)

        // Draw check mark in the second circle
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 8f
        mPaint.style = Paint.Style.STROKE
        mCanvas.drawLine(secondCircleCenterX - 20, startY, secondCircleCenterX, startY + 20, mPaint)
        mCanvas.drawLine(secondCircleCenterX, startY + 20, secondCircleCenterX + 30, startY - 10, mPaint)

        // Draw the line between second and third circles
        val thirdCircleCenterX = startX + circleRadius * 2 + circleSpacing * 2
        mPaint.color = Color.parseColor("#CCCCCC")
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mCanvas.drawLine(secondCircleCenterX + circleRadius, startY, thirdCircleCenterX - circleRadius, startY, mPaint)

        // Draw the third circle (in progress, similar to the previous second circle)
        mPaint.color = Color.parseColor("#229552")
        mPaint.style = Paint.Style.FILL
        mCanvas.drawCircle(thirdCircleCenterX, startY, circleRadius, mPaint)

        // Draw square inside the third circle
        mPaint.color = Color.WHITE
        mCanvas.drawRect(thirdCircleCenterX - 20, startY - 20, thirdCircleCenterX + 20, startY + 20, mPaint)

        // Draw labels below each circle
        mPaint.color = Color.BLACK
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.textSize = 40f
        mCanvas.drawText("Mulai", startX, startY + 100, mPaint)
        mCanvas.drawText("Daftar", secondCircleCenterX, startY + 100, mPaint)
        mCanvas.drawText("Data Diri", thirdCircleCenterX, startY + 100, mPaint)

        // Set the bitmap to the ImageView
        binding.imageViewCustom.setImageBitmap(mBitmap)
    }

    companion object {
        var EXTRA_EMAIL = "extra_email"
        var EXTRA_PASSWORD = "extra_password"
    }


}