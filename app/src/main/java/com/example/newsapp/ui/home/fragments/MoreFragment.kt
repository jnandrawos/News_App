package com.example.newsapp.ui.home.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.newsapp.ui.login.activities.MainActivity
import com.example.newsapp.databinding.FragmentMoreBinding
import com.example.newsapp.ui.home.viewmodels.MoreViewModel
import com.example.newsapp.util.InternalStoragePhoto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private val moreViewModel: MoreViewModel by viewModels()
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentMoreBinding.inflate(layoutInflater, container, false)

        setupViews()
        initObservers()
        implementListeners()

        return binding.root
    }

    private fun setupViews(){
        moreViewModel.showImage()
    }

    private fun implementListeners() {
        binding.tvAboutUs.setOnClickListener {
            goToAboutUs()
        }

        binding.tvEditProfile.setOnClickListener {
            goToEditProfile()
        }

        binding.tvChangePassword.setOnClickListener {
            goToChangePassword()
        }

        binding.logoutButton.setOnClickListener {
            moreViewModel.logout()
        }
    }

    private fun initObservers(){
        moreViewModel.navigateToLogin.observe(viewLifecycleOwner,{hasFinished ->
            if(hasFinished){
                goToLogin()
                moreViewModel.doneNavigateToLogin()
                activity?.finish()
            }

        })

        moreViewModel.showImage.observe(viewLifecycleOwner, { hasSaved ->
            if (hasSaved) {

                viewLifecycleOwner.lifecycleScope.launch {
                    val listOfImages = loadImageFromStorage()
                    for (im in listOfImages) {
                        if (im.name.contains(moreViewModel.getUserEmail())) {
                            Glide.with(requireContext()).load(im.bitmap).circleCrop()
                                .into(binding.civProfile)
                        }
                    }
                }

                moreViewModel.doneSavingImage()
            }
        })
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

    private fun goToLogin() {
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    private fun goToAboutUs(){
        val action =
            MoreFragmentDirections.actionMoreFragmentToAboutUsFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun goToEditProfile() {
        val action =
            MoreFragmentDirections.actionMoreFragmentToEditProfileFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun goToChangePassword() {
        val action =
            MoreFragmentDirections.actionMoreFragmentToChangePasswordFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

}