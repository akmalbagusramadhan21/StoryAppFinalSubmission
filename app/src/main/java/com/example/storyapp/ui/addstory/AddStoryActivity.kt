package com.example.storyapp.ui.addstory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.main.ViewModelFactory
import com.example.storyapp.utils.Result
import com.example.storyapp.utils.getImageUri
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        animateUIElements()
    }
    private fun setupListeners() {
        binding.btnGallery.setOnClickListener {
            galleryLaunch.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnGallery.setOnClickListener {
            galleryLaunch.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnCamera.setOnClickListener {
            currentImageUri = getImageUri(this)
            cameraLaunch.launch(currentImageUri!!)
        }
        binding.buttonAdd.setOnClickListener {
            if (!binding.edAddDescription.text.isNullOrBlank() && currentImageUri != null) {
                currentImageUri?.let { uri ->
                    val imageFile = uriToFile(uri, this).reduceFileImage()
                    val desc = binding.edAddDescription.text.toString()

                    showLoading(true)

                    val requestBody = desc.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                    val multipartBody =
                        MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

                    viewModel.addStory(multipartBody, requestBody)
                        .observe(this) { response ->
                            when (response) {
                                is Result.Error -> showToast(
                                    "Gagal Mengupload Foto Karena ${response.error}"
                                )

                                Result.Loading -> showLoading(
                                    true
                                )

                                is Result.Success -> {
                                    showToast("Berhasil Mengupload Data")
                                    runBlocking {
                                        delay(500)
                                    }
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }
                            }
                        }
                }
            } else {
                showToast("Anda Belum Mengisi Foto")
            }
        }
        }


    private val galleryLaunch = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private val cameraLaunch = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            showImage()
        }else{
            currentImageUri = null

        }

        }

    fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }

    }
    private fun showLoading(loading: Boolean) {
        val animation = if (loading) {
            AlphaAnimation(0f, 1f).apply {
                duration = 300
            }
        } else {
            AlphaAnimation(1f, 0f).apply {
                duration = 300
            }
        }
        binding.progressBar.startAnimation(animation)
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun animateUIElements() {
        val scaleAnim = ScaleAnimation(0f, 1f, 0f, 1f).apply {
            duration = 500
            fillAfter = true
        }
        binding.buttonAdd.startAnimation(scaleAnim)

        val fadeInAnim = AlphaAnimation(0f, 1f).apply {
            duration = 500
            fillAfter = true
        }
        binding.ivPreview.startAnimation(fadeInAnim)

        val fadeInDescriptionAnim = AlphaAnimation(0f, 1f).apply {
            duration = 500
            fillAfter = true
        }
        binding.edAddDescription.startAnimation(fadeInDescriptionAnim)

        val fadeInCameraAnim = AlphaAnimation(0f, 1f).apply {
            duration = 500
            fillAfter = true
        }
        binding.btnCamera.startAnimation(fadeInCameraAnim)

        val fadeInGalleryAnim = AlphaAnimation(0f, 1f).apply {
            duration = 500
            fillAfter = true
        }
        binding.btnGallery.startAnimation(fadeInGalleryAnim)
    }
}