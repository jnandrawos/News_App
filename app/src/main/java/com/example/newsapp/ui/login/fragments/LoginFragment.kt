package com.example.newsapp.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.ui.home.activities.Homepage
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.ui.login.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )

        binding.myLoginViewModel = loginViewModel

        binding.lifecycleOwner = this

        binding.signupTv.setOnClickListener {
            loginViewModel.signUP()
        }

        binding.loginButton.setOnClickListener {
            loginViewModel.loginButton()
        }


        loginViewModel.autoLogin()

        loginViewModel.navigateToSignup.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished == true) {

                goToSignUp()
                loginViewModel.doneNavigatingSignup()
            }
        })

        loginViewModel.errorToast.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT).show()
                loginViewModel.doneToast()
            }
        })

        loginViewModel.errorToastEmail.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.user_not_existing),
                    Toast.LENGTH_SHORT).show()
                loginViewModel.doneToastErrorEmail()
            }
        })

        loginViewModel.errorToastInvalidPassword.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.check_password),
                    Toast.LENGTH_SHORT)
                    .show()
                loginViewModel.doneToastInvalidPassword()
            }
        })

        loginViewModel.navigateToHome.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished == true) {
                goToHomePage()
                activity?.finish()
                loginViewModel.doneNavigatingUserDetails()
            }
        })

        loginViewModel.errorToastEmailFormat.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.wrong_email_format),
                    Toast.LENGTH_SHORT)
                    .show()
                loginViewModel.doneToastErrorEmailFormat()
            }
        })


        return binding.root
    }


    private fun goToSignUp() {

        val action =
            LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        NavHostFragment.findNavController(this).navigate(action)

    }

    private fun goToHomePage() {
        startActivity(Intent(context, Homepage::class.java))
    }


}