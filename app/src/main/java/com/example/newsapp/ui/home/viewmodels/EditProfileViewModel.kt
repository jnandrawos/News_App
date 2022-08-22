package com.example.newsapp.ui.home.viewmodels

import android.app.Application
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.data.repository.EmailPreference
import com.example.newsapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    application: Application,
) :
    AndroidViewModel(application), Observable {

    private val context
        get() = getApplication<Application>()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val userEmail: String = EmailPreference(context).getLoggedInEmail().toString()
    var userFullName: String = ""

    private val _showImage = MutableLiveData<Boolean>()
    val showImage: LiveData<Boolean>
        get() = _showImage

    private val _setName = MutableLiveData<Boolean>()
    val setName: LiveData<Boolean>
        get() = _setName

    private val _updateSuccessful = MutableLiveData<Boolean>()
    val updateSuccessful: LiveData<Boolean>
        get() = _updateSuccessful

    private val _errorToastInvalidName = MutableLiveData<Boolean>()

    val errorToastInvalidName: LiveData<Boolean>
        get() = _errorToastInvalidName


    fun getUserEmail(): String {
        return userEmail
    }

    fun getUserFullName() {
        uiScope.launch {
            val user = repository.getUser(userEmail)
            user?.let {
                userFullName = it.name
                _setName.value = true
            }
        }
    }

    fun updateUser(fullName: String) {
        uiScope.launch {
            val user = repository.getUser(userEmail)
            user?.let {
                if (fullName.isNullOrEmpty()) {
                    _errorToastInvalidName.value = true
                } else {
                    user.name = fullName
                    repository.update(user)
                    _updateSuccessful.value = true
                }
            }
        }
    }

    fun showImage() {
        _showImage.value = true
    }

    fun doneSavingImage() {
        _showImage.value = false
    }

    fun doneSetName() {
        _setName.value = false
    }

    fun doneErrorToastInvalidName() {
        _errorToastInvalidName.value = false
    }

    fun doneUpdateSuccessful() {
        _updateSuccessful.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}
