package com.example.newsapp.ui.home.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.common.toBase64
import com.example.newsapp.common.toBitmap
import com.example.newsapp.databinding.FragmentEditProfileBinding
import com.example.newsapp.ui.home.viewmodels.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var filePath: Uri
    private lateinit var bitmap: Bitmap
    private val getImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                filePath = result
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (!requireContext().contentResolver.equals(null)) {
                        val source =
                            ImageDecoder.createSource(requireContext().contentResolver, filePath)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                }
                editProfileViewModel.updateUserImage(filePath.toBitmap(requireActivity())
                    .toBase64())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)

        setupViews()
        implementListeners()
        initObservers()

        return binding.root
    }

    private fun setupViews() {
        binding.tvEditEmail.text = editProfileViewModel.getUserEmail()
        editProfileViewModel.getUserFullName()
        editProfileViewModel.showImage()
    }

    private fun implementListeners() {
        binding.civEditProfile.setOnClickListener {
            getImage.launch("image/")
        }

        binding.updateButton.setOnClickListener {
            editProfileViewModel.updateUser(binding.etEditFullName.text.toString())
        }
    }

    private fun initObservers() {
        editProfileViewModel.setName.observe(viewLifecycleOwner, { wasSet ->
            if (wasSet) {
                binding.etEditFullName.setText(editProfileViewModel.userFullName)
                editProfileViewModel.doneSetName()
            }
        })

        editProfileViewModel.errorToastInvalidName.observe(viewLifecycleOwner, { hasError ->
            if (hasError) {
                UtilityFunctions.showToast(requireContext(), getString(R.string.invalid_name))
                editProfileViewModel.doneErrorToastInvalidName()
            }
        })

        editProfileViewModel.updateSuccessful.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                UtilityFunctions.showToast(requireContext(), getString(R.string.update_successful))
                goToMore()
                editProfileViewModel.doneUpdateSuccessful()
            }
        })

        editProfileViewModel.showImage.observe(viewLifecycleOwner, { hasSaved ->
            if (hasSaved) {
                try {
                    lifecycleScope.launch {
                        editProfileViewModel.getUserImagePath()?.let {
                            Glide.with((requireActivity()))
                                .load(it.toBitmap())
                                .circleCrop().into(binding.civEditProfile)
                        }
                    }
                } catch (e: Exception) {
                    UtilityFunctions.showToast(requireActivity(), getString(R.string.image_error))
                }
                editProfileViewModel.doneSavingImage()
            }
        })
    }

    private fun goToMore() {
        val action =
            EditProfileFragmentDirections.actionEditProfileFragmentToMoreFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun checkPermissionForReadExternalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

}