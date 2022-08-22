package com.example.newsapp.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.R
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.ui.home.viewmodels.ChangePasswordViewModel
import com.example.newsapp.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        implementListeners()
        initObservers()
    }

    private fun implementListeners() {
        binding.saveButton.setOnClickListener {
            changePasswordViewModel.checkUserPassword(binding.etOldPassword.text.toString(),
                binding.etNewPassword.text.toString())
        }

    }

    private fun initObservers(){

        changePasswordViewModel.errorDisplay.observe(viewLifecycleOwner, { hasError ->
            if (hasError && !(changePasswordViewModel.errorMessage.value.isNullOrEmpty())) {

                UtilityFunctions.showToast(requireContext(),
                    changePasswordViewModel.errorMessage.value.toString())
                changePasswordViewModel.doneToast()
                if(changePasswordViewModel.errorMessage.value.equals(getString(R.string.update_successful)))
                    goToMore()

            }

        })
    }

    private fun goToMore() {
        val action =
            ChangePasswordFragmentDirections.actionChangePasswordFragmentToMoreFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }
}