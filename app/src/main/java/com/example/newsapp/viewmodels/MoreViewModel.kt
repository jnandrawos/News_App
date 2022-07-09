package com.example.newsapp.viewmodels

import android.app.Application
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.repository.EmailPreference
import com.example.newsapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val repository: UserRepository,
    application: Application,
) :
    AndroidViewModel(application), Observable {

    private val context
        get() = getApplication<Application>()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val userEmail: String = EmailPreference(context).getLoggedInEmail().toString()

    private val _showImage = MutableLiveData<Boolean>()
    val showImage: LiveData<Boolean>
        get() = _showImage

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin



    fun showImage() {
        _showImage.value = true
    }

    fun doneSavingImage() {
        _showImage.value = false
    }

    fun getUserEmail(): String {
        return userEmail
    }

    fun logout() {
        EmailPreference(context).setLoggedInEmail("")
        _navigateToLogin.value = true
    }

    fun doneNavigateToLogin(){
        _navigateToLogin.value=false
    }



    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}