package com.example.newsapp.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.ui.home.viewmodels.ChangePasswordViewModel
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentChangePasswordBinding =
            FragmentChangePasswordBinding.inflate(layoutInflater, container, false)

        binding.saveButton.setOnClickListener {
            changePasswordViewModel.checkUserPassword(binding.etOldPassword.text.toString(), binding.etNewPassword.text.toString())
        }

        changePasswordViewModel.errorEmptyToast.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.fill_fields),
                    Toast.LENGTH_SHORT).show()
                changePasswordViewModel.doneErrorEmptyToast()
            }
        })

        changePasswordViewModel.errorNotMatchingToast.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.wrong_password),
                    Toast.LENGTH_SHORT).show()
                changePasswordViewModel.doneErrorNotMatchingToast()
            }
        })

        changePasswordViewModel.passwordSet.observe(viewLifecycleOwner, { hasError ->
            if (hasError == true) {
                Toast.makeText(requireContext(),
                    getString(R.string.update_successful),
                    Toast.LENGTH_SHORT).show()
                goToMore()
                changePasswordViewModel.donePasswordSet()
            }
        })

        return binding.root
    }

    private fun goToMore() {
        val action =
            ChangePasswordFragmentDirections.actionChangePasswordFragmentToMoreFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }
}