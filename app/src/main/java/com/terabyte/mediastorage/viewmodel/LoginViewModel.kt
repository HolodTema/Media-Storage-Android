package com.terabyte.mediastorage.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.TOKEN
import com.terabyte.mediastorage.retrofit.RetrofitManager

class LoginViewModel(application: Application): AndroidViewModel(application) {

    val stateLogin = mutableStateOf("")
    val statePassword = mutableStateOf("")
    val statePasswordVisibility = mutableStateOf(false)

    fun login(successListener: () -> Unit, failureListener: () -> Unit) {
        RetrofitManager.auth(
            TOKEN,
            stateLogin.value,
            statePassword.value,
            {
                
            },
            {
                failureListener()
            }
        )
    }
}