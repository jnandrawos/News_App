package com.example.newsapp.login.presentation.iu.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.common.repository.UserRepository
import com.example.newsapp.home.data.repository.EmailPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    application: Application,
) :
    AndroidViewModel(application), Observable {

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPassword = MutableLiveData<String>()


    private val context
        get() = getApplication<Application>()

    private val _navigateToSignup = MutableLiveData<Boolean>()

    val navigateToSignup: LiveData<Boolean>
        get() = _navigateToSignup

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _errorToast = MutableLiveData<Boolean>()

    val errorToast: LiveData<Boolean>
        get() = _errorToast

    private val _errorToastEmailFormat = MutableLiveData<Boolean>()

    val errorToastEmailFormat: LiveData<Boolean>
        get() = _errorToastEmailFormat

    private val _errorToastEmail = MutableLiveData<Boolean>()

    val errorToastEmail: LiveData<Boolean>
        get() = _errorToastEmail

    private val _errorToastInvalidPassword = MutableLiveData<Boolean>()

    val errorToastInvalidPassword: LiveData<Boolean>
        get() = _errorToastInvalidPassword


    fun signUP() {
        _navigateToSignup.value = true
    }

    fun loginButton() {
        if (inputEmail.value == null || inputPassword.value == null || inputEmail.value == "" || inputPassword.value == "") {
            _errorToast.value = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value.toString()).matches()) {
            _errorToastEmailFormat.value = true
        } else {
            viewModelScope.launch {
                val emails = repository.getUser(inputEmail.value!!)
                if (emails != null) {
                    if (emails.password == inputPassword.value) {
                        EmailPreference(context).setLoggedInEmail(emails.email)
                        inputEmail.value = null
                        inputPassword.value = null
                        _navigateToHome.value = true
                    } else {
                        _errorToastInvalidPassword.value = true
                    }
                } else {
                    _errorToastEmail.value = true
                }
            }
        }
    }


    fun autoLogin() {

        val loggedEmail = EmailPreference(context).getLoggedInEmail()
        if (loggedEmail != "" && loggedEmail!=null) {
            _navigateToHome.value = true
        }
    }


    fun doneNavigatingSignup() {
        _navigateToSignup.value = false
    }

    fun doneNavigatingUserDetails() {
        _navigateToHome.value = false
    }


    fun doneToast() {
        _errorToast.value = false
    }


    fun doneToastErrorEmail() {
        _errorToastEmail.value = false
    }

    fun doneToastErrorEmailFormat() {
        _errorToastEmailFormat.value = false
    }

    fun doneToastInvalidPassword() {
        _errorToastInvalidPassword.value = false

    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}