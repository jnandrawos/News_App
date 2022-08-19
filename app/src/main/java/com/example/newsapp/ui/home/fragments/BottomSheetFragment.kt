package com.example.newsapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.databinding.ImagePickerFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    private var cameraListener: (()->Unit)? =null
    private var storageListener: (()->Unit)? =null

    private lateinit var binding: ImagePickerFragmentBinding

    fun setOnCameraClickListener(listener: (()->Unit)) = apply { this.cameraListener = listener }
    fun setOnStorageClickListener(listener: (()->Unit)) = apply { this.storageListener = listener }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImagePickerFragmentBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }
    private fun initListeners(){
        binding.cameraButton.setOnClickListener{
            cameraListener?.invoke()
        }
        binding.storageButton.setOnClickListener{
            storageListener?.invoke()
        }

    }

}