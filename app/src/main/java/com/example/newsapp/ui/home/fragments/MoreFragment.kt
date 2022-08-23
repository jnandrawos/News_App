package com.example.newsapp.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.common.toBitmap
import com.example.newsapp.databinding.FragmentMoreBinding
import com.example.newsapp.ui.home.viewmodels.MoreViewModel
import com.example.newsapp.ui.login.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private val moreViewModel: MoreViewModel by viewModels()
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater, container, false)

        setupViews()
        initObservers()
        implementListeners()

        return binding.root
    }

    private fun setupViews() {
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

    private fun initObservers() {
        moreViewModel.navigateToLogin.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                goToLogin()
                moreViewModel.doneNavigateToLogin()
                activity?.finish()
            }

        })

        moreViewModel.showImage.observe(viewLifecycleOwner, { hasSaved ->
            if (hasSaved) {

                try {
                    lifecycleScope.launch {
                        moreViewModel.getUserImagePath()?.let {
                            if (it.isEmpty())
                                Glide.with(requireActivity())
                                    .load(R.drawable.profile_image_placeholder).circleCrop()
                                    .into(binding.civProfile)
                            else
                                Glide.with(requireActivity())
                                    .load(it.toBitmap())
                                    .circleCrop().into(binding.civProfile)
                        }
                    }
                } catch (e: Exception) {
                    UtilityFunctions.showToast(requireActivity(), getString(R.string.image_error))
                }

                moreViewModel.doneSavingImage()
            }
        })
    }


    private fun goToLogin() {
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    private fun goToAboutUs() {
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