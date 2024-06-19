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
import androidx.appcompat.app.AlertDialog
import com.example.capstone.R
import com.example.capstone.databinding.FragmentRegisterCredentialBinding
import com.example.capstone.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class RegisterCredentialFragment : Fragment() {

    private lateinit var binding : FragmentRegisterCredentialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterCredentialBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawProgressIndicator()

        setupAction()

        toLogin()
    }

    private fun setupAction() {
        binding.buttonStart.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val passwordConf = binding.passwordConfirmEditText.text.toString()
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConf)) {
                if (password.length < 8) {
                    showAlert(
                        getString(R.string.register_gagal),
                        getString(R.string.password_too_short)
                    ){}
                } else if (password == passwordConf) {
                    moveToNext(email, password)
                } else {
                    showAlert(
                        getString(R.string.register_gagal),
                        getString(R.string.register_gagal_2)
                    ){}
                }
            } else {
                showAlert(
                    getString(R.string.register_gagal),
                    getString(R.string.register_gagal_1)
                ) { }
            }
        }
    }

    private fun moveToNext(email : String, password : String) {
        val registerDataFragment = RegisterDataFragment()

        val bundle = Bundle()
        bundle.putString(RegisterDataFragment.EXTRA_EMAIL, email)
        bundle.putString(RegisterDataFragment.EXTRA_PASSWORD, password)

        registerDataFragment.arguments = bundle

        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.main, registerDataFragment, RegisterDataFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
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

    private fun toLogin() {
        binding.textButtonSignIn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
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

        // Draw the second circle (in progress)
        val secondCircleCenterX = startX + circleRadius + circleSpacing
        mPaint.color = Color.parseColor("#229552")
        mPaint.style = Paint.Style.FILL
        mCanvas.drawCircle(secondCircleCenterX, startY, circleRadius, mPaint)

        // Draw square inside the second circle
        mPaint.color = Color.WHITE
        mCanvas.drawRect(startX + circleRadius + circleSpacing - 20, startY - 20, startX + circleRadius + circleSpacing + 20, startY + 20, mPaint)

        // Draw the line between second and third circles correctly

        val thirdCircleCenterX = startX + circleRadius * 2 + circleSpacing * 2
        mPaint.color = Color.parseColor("#CCCCCC")
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mCanvas.drawLine(secondCircleCenterX + circleRadius, startY, thirdCircleCenterX - circleRadius, startY, mPaint)

        // Draw the third circle (not started)
        mPaint.color = Color.parseColor("#CCCCCC")
        mPaint.style = Paint.Style.STROKE
        mCanvas.drawCircle(thirdCircleCenterX, startY, circleRadius, mPaint)

        // Draw labels below each circle
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.BLACK
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.textSize = 40f
        mCanvas.drawText("Mulai", startX, startY + 100, mPaint)
        mCanvas.drawText("Daftar", secondCircleCenterX, startY + 100, mPaint)
        mCanvas.drawText("Data Diri", thirdCircleCenterX, startY + 100, mPaint)

        // Set the bitmap to the ImageView
        binding.imageViewCustom.setImageBitmap(mBitmap)
    }


}