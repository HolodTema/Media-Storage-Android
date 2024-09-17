package com.terabyte.mediastorage.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.activity.LoginActivity
import com.terabyte.mediastorage.activity.PhotoInfoActivity
import com.terabyte.mediastorage.activity.room.RoomManager
import com.terabyte.mediastorage.activity.room.UploadingHistoryItem
import com.terabyte.mediastorage.json.ItemJson
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.model.MemoryUsageModel
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.util.BitmapConverter
import com.terabyte.mediastorage.util.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

class MainViewModel(private val application: Application): AndroidViewModel(application) {
    private val memoryUsageModel = MemoryUsageModel()

    val stateUsername = mutableStateOf("")
    val stateEmail = mutableStateOf("")
    val stateFailureRequest = mutableStateOf(false)
    val stateItems = mutableStateOf<ArrayList<ItemModel>?>(null)
    val stateAmountItems = mutableStateOf(0)
    val stateMemoryUsage = mutableStateOf( memoryUsageModel.defaultValue())
    val stateUploadingHistory = mutableStateOf(listOf<UploadingHistoryItem>())


    init {
        initRequests()
    }

    fun initRequests() {
        getUserInfo()
        getAllItems()
        getUploadingHistory()
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

    private fun getAllItems() {
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

    private fun getItemsData(accessToken: String, itemJsonList: List<ItemJson>) {
        itemJsonList.forEach { itemJson ->
            RetrofitManager.getItem(
                application.applicationContext,
                accessToken,
                itemJson.id,
                successListener = { bytes ->
                    stateMemoryUsage.value = memoryUsageModel.calculateMemoryUsage(bytes)
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

    private fun itemJsonToItemModel(itemJson: ItemJson, bytes: ByteArray) {
        BitmapConverter.itemJsonToItemModel(application.applicationContext, itemJson, bytes) {
            if(stateItems.value==null) stateItems.value = arrayListOf(it)
            else stateItems.value!!.add(it)
        }
    }

    private fun getUploadingHistory() {
        RoomManager.getUploadingHistory(application.applicationContext) {
            stateUploadingHistory.value = it
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

    fun uploadImage(imageUri: Uri, successListener: () -> Unit, failureListener: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                val bitmap = BitmapConverter.bitmapFromLocalUri(application.applicationContext, imageUri)
                if(bitmap==null) null
                else {
                    val buffer = ByteBuffer.allocate(bitmap.byteCount)
                    bitmap.copyPixelsToBuffer(buffer)
                    val bytes = ByteArray(bitmap.byteCount)
                    buffer.rewind()
                    buffer.get(bytes)
                    bytes
                }
            }
            val bytes = deferred.await()
            if(bytes==null) {
                failureListener()
            }
            else {
//                RetrofitManager.uploadItem(application.applicationContext, )
            }
        }
    }

}