package com.example.newsapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentEditProfileBinding
import com.example.newsapp.util.Constants
import com.example.newsapp.util.InternalStoragePhoto
import com.example.newsapp.viewmodels.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private lateinit var filePath: Uri
    private lateinit var bitmap: Bitmap
    private val getImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->


            if (result != null) {
                filePath = result
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver!!, filePath)
                    bitmap = ImageDecoder.decodeBitmap(source)
                }
                saveImageToStorage(editProfileViewModel.getUserEmail(), bitmap)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentEditProfileBinding =
            FragmentEditProfileBinding.inflate(layoutInflater, container, false)

        binding.tvEditEmail.text = editProfileViewModel.getUserEmail()
        editProfileViewModel.getUserFullName()


        binding.civEditProfile.setOnClickListener {
            getImage.launch("image/")
        }

        binding.updateButton.setOnClickListener {
            editProfileViewModel.updateUser(binding.etEditFullName.text.toString())
        }


        editProfileViewModel.showImage()

        editProfileViewModel.setName.observe(viewLifecycleOwner, { wasSet ->
            if (wasSet == true) {
                binding.etEditFullName.setText(editProfileViewModel.userFullName)
                editProfileViewModel.doneSetName()
            }
        })

        editProfileViewModel.errorToastInvalidName.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.invalid_name),
                    Toast.LENGTH_SHORT).show()
                editProfileViewModel.doneErrorToastInvalidName()
            }
        })

        editProfileViewModel.updateSuccessful.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.update_successful),
                    Toast.LENGTH_SHORT).show()
                goToMore()
                editProfileViewModel.doneUpdateSuccessful()
            }
        })

        editProfileViewModel.showImage.observe(viewLifecycleOwner, { hasSaved ->
            if (hasSaved == true) {

                viewLifecycleOwner.lifecycleScope.launch {
                    val listOfImages = loadImageFromStorage()
                    for (im in listOfImages) {
                        if (im.name.contains(editProfileViewModel.getUserEmail())) {
                            Glide.with(requireContext()).load(im.bitmap).circleCrop()
                                .into(binding.civEditProfile)
                        }
                    }
                }

                editProfileViewModel.doneSavingImage()
            }
        })


        return binding.root
    }


    private fun goToMore() {
        val action =
            EditProfileFragmentDirections.actionEditProfileFragmentToMoreFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun checkPermissionForReadExternalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }


    private fun saveImageToStorage(fileName: String, bitmap: Bitmap): Boolean {

        return try {
            requireContext().openFileOutput("$fileName.jpg", Context.MODE_PRIVATE)
                .use { outputStream ->
                    editProfileViewModel.showImage()
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException(getString(R.string.bitmap_error))
                    }
                }

            true
        } catch (e: IOException) {
            Log.e(getString(R.string.error), e.toString())
            false
        }
    }

    private suspend fun loadImageFromStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = requireContext().filesDir.listFiles()
            files.filter {
                it.canRead() && it.name.endsWith(".jpg")
            }.map {
                val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bitmap)
            }
        }
    }


}