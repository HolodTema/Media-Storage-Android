package com.terabyte.mediastorage.viewmodel

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.activity.LoginActivity
import com.terabyte.mediastorage.activity.PhotoInfoActivity
import com.terabyte.mediastorage.json.ItemJson
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.util.BitmapConverter
import com.terabyte.mediastorage.util.DataStoreManager

class MainViewModel(private val application: Application): AndroidViewModel(application) {
    val stateUsername = mutableStateOf("")
    val stateEmail = mutableStateOf("")
    val stateFailureRequest = mutableStateOf(false)
    val stateItems = mutableStateOf<ArrayList<ItemModel>?>(null)
    val stateAmountItems = mutableStateOf(0)

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
                        stateAmountItems.value = itemJsonList.size
                        getItemsData(token, itemJsonList)
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

    fun getItemsData(accessToken: String, itemJsonList: List<ItemJson>) {
        itemJsonList.forEach { itemJson ->
            RetrofitManager.getItem(
                application.applicationContext,
                accessToken,
                itemJson.id,
                successListener = { bytes ->
                    itemJsonToItemModel(itemJson, bytes)
                },
                unauthorizedListener = {
                    startLoginActivity(accessToken)
                },
                failureListener = {
                    stateFailureRequest.value = true
                }
            )
        }
    }

    fun itemJsonToItemModel(itemJson: ItemJson, bytes: ByteArray) {
        BitmapConverter.itemJsonToItemModel(application.applicationContext, itemJson, bytes) {
            if(stateItems.value==null) stateItems.value = arrayListOf(it)
            else stateItems.value!!.add(it)
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

    fun startPhotoInfoActivity() {
        val intent = Intent(application.applicationContext, PhotoInfoActivity::class.java)
        startActivity(application.applicationContext, intent, null)
    }

}