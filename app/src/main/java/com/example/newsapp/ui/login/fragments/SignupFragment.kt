package com.example.newsapp.ui.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.R
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.databinding.FragmentSignupBinding
import com.example.newsapp.ui.login.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val signupViewModel: SignupViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup, container, false
        )
        binding.myViewModel = signupViewModel
        binding.lifecycleOwner = this

        implementListeners()
        initObservers()

        return binding.root
    }

    private fun implementListeners() {
        binding.signupButton.setOnClickListener {
            signupViewModel.submitButton()
        }
    }

    private fun initObservers() {
        signupViewModel.navigateTo.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                goToLogin()
                signupViewModel.doneNavigating()
            }
        })

        signupViewModel.successfulSignUp.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                UtilityFunctions.showToast(requireContext(), getString(R.string.successful_signup))
                signupViewModel.doneSuccessfulSignUp()
            }
        })

        signupViewModel.errorDisplay.observe(viewLifecycleOwner, { hasError ->
            if (hasError && !(signupViewModel.errorMessage.value.isNullOrEmpty())) {
                UtilityFunctions.showToast(requireContext(),
                    signupViewModel.errorMessage.value.toString())
                signupViewModel.doneToast()
            }
        })
    }

    private fun goToLogin() {
        val action =
            SignupFragmentDirections.actionSignupFragmentToLoginFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }
}