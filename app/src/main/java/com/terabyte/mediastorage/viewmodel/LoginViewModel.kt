package com.terabyte.mediastorage.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.TOKEN
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.util.DataStoreManager

class LoginViewModel(private val application: Application): AndroidViewModel(application) {
    val stateLogin = mutableStateOf("")
    val statePassword = mutableStateOf("")
    val statePasswordVisibility = mutableStateOf(false)

    init {
        DataStoreManager.readFromDataStore(application.applicationContext, DataStoreManager.Keys.LOGIN) { login ->
            if(login!=null) {
                DataStoreManager.readFromDataStore(application.applicationContext, DataStoreManager.Keys.PASSWORD) { password ->
                    if(password!=null) {
                        stateLogin.value = login
                        statePassword.value = password
                    }
                }
            }
        }
    }

    fun login(successListener: () -> Unit, failureListener: () -> Unit) {
        RetrofitManager.auth(
            application.applicationContext,
            stateLogin.value,
            statePassword.value,
            { authResponse ->
                DataStoreManager.saveToDataStore(application.applicationContext, DataStoreManager.Keys.LOGIN, stateLogin.value)
                DataStoreManager.saveToDataStore(application.applicationContext, DataStoreManager.Keys.PASSWORD, statePassword.value)
                DataStoreManager.saveToDataStore(application.applicationContext, DataStoreManager.Keys.ACCESS_TOKEN, authResponse.accessToken)
                successListener()
            },
            {
                failureListener()
            }
        )
    }

    private fun rememberSuccessLoginInDataStore() {
        TODO("Not yet implemented")
    }
}