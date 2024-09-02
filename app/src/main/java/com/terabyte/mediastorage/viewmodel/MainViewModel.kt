package com.terabyte.mediastorage.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel(application: Application): AndroidViewModel(application) {
    val stateLogin = mutableStateOf("")
    val statePassword = mutableStateOf("")
    val statePasswordVisibility = mutableStateOf(false)

}