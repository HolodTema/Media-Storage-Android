package com.terabyte.mediastorage.viewmodel

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.activity.LoginActivity
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.util.BitmapConverter
import com.terabyte.mediastorage.util.DataStoreManager

class MainViewModel(private val application: Application): AndroidViewModel(application) {
    val stateUsername = mutableStateOf("")
    val stateEmail = mutableStateOf("")
    val stateFailureRequest = mutableStateOf(false)
    val stateItems = mutableStateOf<List<ItemModel>?>(null)

    init {
        initRequests()
    }

    fun initRequests() {
        getUserInfo()
        getAllItems()
    }

    fun getUserInfo() {
        stateFailureRequest.value = false
        DataStoreManager.readFromDataStore(application.applicationContext, DataStoreManager.Keys.ACCESS_TOKEN) { token ->
            if(token==null) {
                startLoginActivity()
            }
            else {
                RetrofitManager.getCurrentUser(
                    application.applicationContext,
                    token,
                    successListener = {
                        stateFailureRequest.value = false
                        stateUsername.value = it.name
                        stateEmail.value = it.email
                    },
                    unauthorizedListener = {
                        startLoginActivity(token)
                    },
                    failureListener = {
                        stateFailureRequest.value = true
                    }
                )
            }
        }
    }

    fun getAllItems() {
        DataStoreManager.readFromDataStore(application.applicationContext, DataStoreManager.Keys.ACCESS_TOKEN) { token ->
            if(token==null) startLoginActivity()
            else {
                RetrofitManager.getAllItems(
                    application.applicationContext,
                    token,
                    successListener = { itemJsonList ->
                        BitmapConverter.itemJsonToItemModel(application.applicationContext, itemJsonList) { itemModelList ->
                            stateItems.value = itemModelList
                        }
                    },
                    unauthorizedListener = {
                        startLoginActivity(token)
                    },
                    failureListener = {
                        stateFailureRequest.value = true
                    }
                )
            }
        }
    }

    fun logout() {
        DataStoreManager.readFromDataStore(application.applicationContext, DataStoreManager.Keys.ACCESS_TOKEN) { token ->
            if(token==null) {
                startLoginActivity()
            }
            else {
                RetrofitManager.logout(application.applicationContext, token,
                    successListener = {
                        DataStoreManager.deleteFromDataStore(application.applicationContext, DataStoreManager.Keys.LOGIN)
                        DataStoreManager.deleteFromDataStore(application.applicationContext, DataStoreManager.Keys.PASSWORD)
                        startLoginActivity(token)
                    },
                    failureListener = {
                        stateFailureRequest.value = true
                    }
                )
            }
        }
    }

    private fun startLoginActivity(token: String? = null) {
        if(token!=null) DataStoreManager.deleteFromDataStore(application.applicationContext, DataStoreManager.Keys.ACCESS_TOKEN)
        application.startActivity(
            Intent(application.applicationContext, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

}