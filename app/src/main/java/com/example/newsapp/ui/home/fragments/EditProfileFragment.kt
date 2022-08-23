package com.example.newsapp.ui.home.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.newsapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var filePath: Uri
    private var isBottomSheetUp: Boolean = false
    private var bottomSheet = BottomSheetFragment()
    private lateinit var bitmap: Bitmap
    private val getImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let {
                filePath = result
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    requireContext().contentResolver?.let {
                        val source =
                            ImageDecoder.createSource(it, filePath)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                }
                editProfileViewModel.updateUserImage(filePath.toBitmap(requireActivity())
                    .toBase64())
                bottomSheet.dismiss()
            }
        }
    private val getImageFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.extras?.get("data") as? Bitmap
                photo?.let {
                    editProfileViewModel.updateUserImage(it
                        .toBase64())
                    bottomSheet.dismiss()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        implementListeners()
        initObservers()
    }

    private fun setupViews() {
        binding.tvEditEmail.text = editProfileViewModel.getUserEmail()
        editProfileViewModel.getUserFullName()
        editProfileViewModel.showImage()
    }

    private fun implementListeners() {
        binding.civEditProfile.setOnClickListener {
            bottomSheet.apply {
                isBottomSheetUp = true
                setOnCameraClickListener {
                    if (setupPermissions()) {
                        getImageFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    } else {
                        makeRequest()
                    }
                }
                setOnStorageClickListener {
                    getImage.launch("image/")
                }
            }.show(parentFragmentManager, getString(R.string.bottom_sheet_tag))
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
                            if (it.isEmpty())
                                Glide.with(requireActivity())
                                    .load(R.drawable.profile_image_placeholder).circleCrop()
                                    .into(binding.civEditProfile)
                            else
                                Glide.with(requireActivity())
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

    private fun setupPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            UtilityFunctions.printLogs(getString(R.string.error),
                getString(R.string.permission_error))
            return false
        }
        return true
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            Constants.CAMERA_REQUEST_CODE)
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