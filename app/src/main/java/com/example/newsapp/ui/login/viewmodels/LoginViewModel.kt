package com.example.newsapp.ui.login.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.repository.UserRepository
import com.example.newsapp.data.repository.EmailPreference
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
    private val _errorDisplay = MutableLiveData<Boolean>()
    val errorDisplay: LiveData<Boolean>
        get() = _errorDisplay
    var errorMessage = MutableLiveData<String>()

    fun signUP() {
        _navigateToSignup.value = true
    }

    fun loginButton() {
        if (inputPassword.value.isNullOrEmpty() || inputEmail.value.isNullOrEmpty()) {
            errorMessage.value = context.resources.getString(R.string.fill_fields)
            _errorDisplay.value = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value.toString()).matches()) {
            errorMessage.value = context.resources.getString(R.string.wrong_email_format)
            _errorDisplay.value = true
        } else {
            viewModelScope.launch {
                val emails = repository.getUser(inputEmail.value.toString())
                if (emails != null) {
                    if (emails.password.equals(inputPassword.value)) {
                        EmailPreference(context).setLoggedInEmail(emails.email)
                        inputEmail.value = null
                        inputPassword.value = null
                        _navigateToHome.value = true
                    } else {
                        errorMessage.value = context.resources.getString(R.string.check_password)
                        _errorDisplay.value = true
                    }
                } else {
                    errorMessage.value = context.resources.getString(R.string.user_not_existing)
                    _errorDisplay.value = true
                }
            }
        }
    }


    fun autoLogin() {
        val loggedEmail = EmailPreference(context).getLoggedInEmail()
        if (!loggedEmail.isNullOrEmpty()) {
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
        _errorDisplay.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}