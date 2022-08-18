package com.example.newsapp.ui.home.viewmodels

import android.app.Application
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.repository.EmailPreference
import com.example.newsapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repository: UserRepository,
    application: Application,
) :
    AndroidViewModel(application), Observable {

    private val context
        get() = getApplication<Application>()

    private val userEmail: String = EmailPreference(context).getLoggedInEmail().toString()

    private val _errorDisplay = MutableLiveData<Boolean>()

    val errorDisplay: LiveData<Boolean>
        get() = _errorDisplay

    var errorMessage = MutableLiveData<String>()

    fun checkUserPassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            val user = repository.getUser(userEmail)
            if (user != null) {
                if (oldPassword.isNullOrEmpty() || newPassword.isNullOrEmpty()) {
                    errorMessage.value = context.resources.getString(R.string.fill_fields)
                    _errorDisplay.value = true
                } else if (!(oldPassword.equals(user.password))) {
                    errorMessage.value = context.resources.getString(R.string.wrong_password)
                    _errorDisplay.value = true
                } else {
                    user.password = newPassword
                    repository.update(user)
                    errorMessage.value = context.resources.getString(R.string.update_successful)
                    _errorDisplay.value = true
                }
            }
        }
    }

    fun doneToast() {
        _errorDisplay.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}