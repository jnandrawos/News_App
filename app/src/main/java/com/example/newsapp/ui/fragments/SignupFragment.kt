package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSignupBinding
import com.example.newsapp.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignupBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup, container, false
        )

        binding.myViewModel = signupViewModel
        binding.lifecycleOwner = this

        binding.signupButton.setOnClickListener {
            signupViewModel.submitButton()
        }


        signupViewModel.navigateTo.observe(viewLifecycleOwner,  { hasFinished->
            if (hasFinished == true){
                goToLogin()
                signupViewModel.doneNavigating()
            }
        })

        signupViewModel.successfulSignUp.observe(viewLifecycleOwner,  { hasFinished->
            if(hasFinished==true){
                Toast.makeText(requireContext(), getString(R.string.successful_signup), Toast.LENGTH_SHORT).show()
                signupViewModel.doneSuccessfulSignUp()
            }
        })

        signupViewModel.errorToast.observe(viewLifecycleOwner,  { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                signupViewModel.doneToast()
            }
        })

        signupViewModel.errorToastEmail.observe(viewLifecycleOwner,  { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), getString(R.string.email_already_registered), Toast.LENGTH_SHORT).show()
                signupViewModel.doneToastEmail()
            }
        })

        signupViewModel.errorToastEmailFormat.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(), getString(R.string.wrong_email_format), Toast.LENGTH_SHORT)
                    .show()
                signupViewModel.doneToastErrorEmailFormat()
            }
        })

        signupViewModel.errorToastPasswordMismatch.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(), getString(R.string.passwords_not_matching), Toast.LENGTH_SHORT)
                    .show()
                signupViewModel.doneToastErrorPasswordMismatch()
            }
        })

        return binding.root
    }

    private fun goToLogin() {
        val action =
            SignupFragmentDirections.actionSignupFragmentToLoginFragment()
        NavHostFragment.findNavController(this).navigate(action)

    }

}