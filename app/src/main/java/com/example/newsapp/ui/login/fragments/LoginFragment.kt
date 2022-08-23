package com.example.newsapp.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.ui.home.activities.HomepageActivity
import com.example.newsapp.R
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.ui.login.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )

        binding.myLoginViewModel = loginViewModel
        binding.lifecycleOwner = this
        implementListeners()
        loginViewModel.autoLogin()
        initObservers()

        return binding.root
    }

    private fun implementListeners() {
        binding.signupTv.setOnClickListener {
            loginViewModel.signUP()
        }
        binding.loginButton.setOnClickListener {
            loginViewModel.loginButton()
        }
    }

    private fun initObservers() {
        loginViewModel.errorDisplay.observe(viewLifecycleOwner, { hasError ->
            if (hasError && !(loginViewModel.errorMessage.value.isNullOrEmpty())) {
                UtilityFunctions.showToast(requireContext(),
                    loginViewModel.errorMessage.value.toString())
                loginViewModel.doneToast()
            }

        })

        loginViewModel.navigateToSignup.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                goToSignUp()
                loginViewModel.doneNavigatingSignup()
            }
        })

        loginViewModel.navigateToHome.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                goToHomePage()
                activity?.finish()
                loginViewModel.doneNavigatingUserDetails()
            }
        })
    }

    private fun goToSignUp() {
        val action =
            LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun goToHomePage() {
        startActivity(Intent(context, HomepageActivity::class.java))
    }
}