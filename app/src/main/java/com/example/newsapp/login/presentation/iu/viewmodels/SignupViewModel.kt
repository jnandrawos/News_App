package com.example.newsapp.login.presentation.iu.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.common.database.UserEntity
import com.example.newsapp.common.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: UserRepository, application: Application) :
    AndroidViewModel(application), Observable {


    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPassword = MutableLiveData<String>()

    @Bindable
    val inputConfirmPassword = MutableLiveData<String>()

    @Bindable
    val inputName = MutableLiveData<String>()



    private val _navigateTo = MutableLiveData<Boolean>()

    val navigateTo: LiveData<Boolean>
        get() = _navigateTo

    private val _successfulSignUp = MutableLiveData<Boolean>()

    val successfulSignUp: LiveData<Boolean>
        get() = _successfulSignUp

    private val _errorToast = MutableLiveData<Boolean>()

    val errorToast: LiveData<Boolean>
        get() = _errorToast

    private val _errorToastEmail = MutableLiveData<Boolean>()

    val errorToastEmail: LiveData<Boolean>
        get() = _errorToastEmail

    private val _errorToastEmailFormat = MutableLiveData<Boolean>()

    val errorToastEmailFormat: LiveData<Boolean>
        get() = _errorToastEmailFormat

    private val _errorToastPasswordMismatch = MutableLiveData<Boolean>()

    val errorToastPasswordMismatch: LiveData<Boolean>
        get() = _errorToastPasswordMismatch

    fun submitButton() {

        if (inputEmail.value == null || inputPassword.value == null || inputConfirmPassword.value == null || inputName.value == null
            || inputEmail.value == "" || inputPassword.value == "" || inputConfirmPassword.value == "" || inputName.value == ""
        ) {
            _errorToast.value = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value.toString()).matches()) {
            _errorToastEmailFormat.value = true
        } else if (inputPassword.value!=inputConfirmPassword.value) {
            _errorToastPasswordMismatch.value = true
        } else {
            viewModelScope.launch {

                val emails = repository.getUser(inputEmail.value!!)
                if (emails != null) {
                    _errorToastEmail.value = true
                } else {
                    val email = inputEmail.value!!
                    val password = inputPassword.value!!
                    val name = inputName.value!!

                    insert(UserEntity(email, password, name))
                    inputEmail.value = null
                    inputPassword.value = null
                    inputConfirmPassword.value = null
                    inputName.value = null
                    _successfulSignUp.value = true
                    _navigateTo.value = true
                }
            }
        }
    }


    fun doneNavigating() {
        _navigateTo.value = false
    }

    fun doneToast() {
        _errorToast.value = false
    }

    fun doneToastEmail() {
        _errorToast.value = false
    }

    fun doneSuccessfulSignUp(){
        _successfulSignUp.value = false
    }
    fun doneToastErrorEmailFormat() {
        _errorToastEmailFormat.value = false
    }

    fun doneToastErrorPasswordMismatch() {
        _errorToastPasswordMismatch.value = false
    }


    private fun insert(user: UserEntity): Job = viewModelScope.launch {
        repository.insert(user)
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}
