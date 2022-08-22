package com.example.newsapp.ui.home.viewmodels

import android.app.Application
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    private val _errorEmptyToast = MutableLiveData<Boolean>()

    val errorEmptyToast: LiveData<Boolean>
        get() = _errorEmptyToast

    private val _errorNotMatchingToast = MutableLiveData<Boolean>()

    val errorNotMatchingToast: LiveData<Boolean>
        get() = _errorNotMatchingToast

    private val _passwordSet = MutableLiveData<Boolean>()

    val passwordSet: LiveData<Boolean>
        get() = _passwordSet

    fun checkUserPassword(oldPassword: String, newPassword: String) {

        viewModelScope.launch {
            val user = repository.getUser(userEmail)
            if (user != null) {
                if(oldPassword == null || newPassword == null || oldPassword == "" || newPassword == "" ){
                    _errorEmptyToast.value = true
                }else if(oldPassword != user.password){
                    _errorNotMatchingToast.value = true
                }else{
                    user.password = newPassword
                    repository.update(user)
                    _passwordSet.value = true
                }
            }
        }
    }

    fun doneErrorEmptyToast(){
        _errorEmptyToast.value = false
    }

    fun doneErrorNotMatchingToast(){
        _errorNotMatchingToast.value = false
    }

    fun donePasswordSet(){
        _passwordSet.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}