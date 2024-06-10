package com.example.capstone.ui.analyze

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.data.ResultState
import com.example.capstone.data.api.response.DataPredict
import com.example.capstone.databinding.FragmentAnalyzeBinding
import com.example.capstone.ui.ViewModelFactory
import com.example.capstone.ui.analyze.detail.DetailAnalyzeActivity
import com.example.capstone.ui.camera.CameraActivity
import com.example.capstone.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.capstone.utils.getImageUri
import com.example.capstone.utils.reduceFileImage
import com.example.capstone.utils.uriToFile

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.apply {
            galleryButton.setOnClickListener { startGallery() }
            cameraButton.setOnClickListener { startCamera() }
            analyzeButton.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            val imageUriString = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
            Log.d("AnalyzeFragment", "CameraX Image URI: $imageUriString")
            currentImageUri = imageUriString?.toUri()
            showImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            Log.d("AnalyzeFragment", "Upload Image URI: $uri")
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("AnalyzeFragment", "Image File Path: ${imageFile.absolutePath}")
            showLoading(true)

            viewModel.uploadImage(imageFile).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            val data = result.data.data
                            if (data != null) {
                                moveToResult(currentImageUri.toString(), data)
                            } else {
                                showToast(getString(R.string.error_null_data))
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
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun moveToResult(result : String, data : DataPredict) {
        val intent = Intent(requireContext(), DetailAnalyzeActivity::class.java)
        intent.putExtra(DetailAnalyzeActivity.EXTRA_IMAGE_URI, result)
        intent.putExtra(DetailAnalyzeActivity.DATA_DETAIL, data)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}