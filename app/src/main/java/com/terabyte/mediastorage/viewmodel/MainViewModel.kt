package com.terabyte.mediastorage.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.activity.LoginActivity
import com.terabyte.mediastorage.activity.room.RoomManager
import com.terabyte.mediastorage.activity.room.UploadingHistoryItem
import com.terabyte.mediastorage.json.ItemJson
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.model.MemoryUsageModel
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.util.AccessTokenManager
import com.terabyte.mediastorage.util.BitmapManager
import com.terabyte.mediastorage.util.UserManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(private val application: Application): AndroidViewModel(application) {
    private var isItemDeleted = false

    private val memoryUsageModel = MemoryUsageModel()

    val stateUsername = mutableStateOf("")
    val stateEmail = mutableStateOf("")
    val stateFailureRequest = mutableStateOf(false)
    val stateItems = mutableStateOf<List<ItemModel>?>(null)
    val stateAmountItems = mutableIntStateOf(0)
    val stateMemoryUsage = mutableStateOf( memoryUsageModel.defaultValue())
    val stateUploadingHistory = mutableStateOf(listOf<UploadingHistoryItem>())


    init {
        getUserInfo()
        getAllItems()
        getUploadingHistory()
    }

    fun getUserInfo() {
        if(UserManager.user==null) {
            RetrofitManager.getCurrentUser(
                application.applicationContext,
                AccessTokenManager.getAccessToken(),
                successListener = {
                    UserManager.cache(it)
                    stateUsername.value = UserManager.user!!.name
                    stateEmail.value = UserManager.user!!.email
                },
                unauthorizedListener = ::defaultUnauthorizedListener,
                failureListener = ::defaultFailureListener)
        }
        else {
            stateUsername.value = UserManager.user!!.name
            stateEmail.value = UserManager.user!!.email
        }

    }

    fun uploadImage(uri: Uri, successListener: () -> Unit, noBitmapListener: () -> Unit) {
        val token = AccessTokenManager.getAccessToken()

        BitmapManager.getBitmapAndFileFromContentUri(application.applicationContext, uri) { pair ->
            val bitmap = pair.first
            val file = pair.second

            if(file==null || bitmap==null) {
                noBitmapListener()
            }
            else {
                RetrofitManager.uploadItem(
                    application.applicationContext,
                    token,
                    file.name,
                    UserManager.user!!.id,
                    file,
                    successListener = { itemJson ->
                        getItemData(token, itemJson) {
                            stateAmountItems.value ++
                        }
                        insertToUploadingHistory(itemJson, file.absolutePath, bitmap)
                        successListener()
                    },
                    unauthorizedListener = ::defaultUnauthorizedListener,
                    failureListener = ::defaultFailureListener
                )
            }
        }
    }

    private fun insertToUploadingHistory(itemJson: ItemJson, filePath: String, bitmap: Bitmap) {
        val uploadingHistoryItem =  UploadingHistoryItem(
            0L,
            itemJson.filename,
            SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.US).format(Date()),
            filePath,
            bitmap.asImageBitmap()
        )
        RoomManager.insertUploadingHistoryItem(
            application.applicationContext,
            uploadingHistoryItem
        )
        stateUploadingHistory.value = stateUploadingHistory.value.plus(uploadingHistoryItem)
    }

    fun logout() {
        val token = AccessTokenManager.getAccessToken()
        RetrofitManager.logout(application.applicationContext, token,
            successListener = {
                AccessTokenManager.deleteAccessToken(application.applicationContext)
                startLoginActivity()
            },
            failureListener = ::defaultFailureListener
        )
    }


    private fun getAllItems() {
        val token = AccessTokenManager.getAccessToken()
        RetrofitManager.getAllItems(
            application.applicationContext,
            token,
            successListener = { itemJsonList ->
                stateAmountItems.intValue = itemJsonList.size
                getItemsData(itemJsonList)
            },
            unauthorizedListener = ::defaultUnauthorizedListener,
            failureListener = ::defaultFailureListener
        )
    }

    private fun getItemsData(itemJsonList: List<ItemJson>) {
        val token = AccessTokenManager.getAccessToken()
        itemJsonList.forEach { itemJson ->
            getItemData(token, itemJson)
        }
    }

    private fun getItemData(token: String, item: ItemJson, successListener: () -> Unit = {}) {
        RetrofitManager.getItem(
            application.applicationContext,
            token,
            item.id,
            successListener = { bytes ->
                calculateMemoryUsage(bytes)
                updateStateItems(item, bytes)
                successListener()
            },
            unauthorizedListener = ::defaultUnauthorizedListener,
            failureListener = ::defaultFailureListener
        )
    }

    private fun updateStateItems(itemJson: ItemJson, bytes: ByteArray) {
        BitmapManager.itemJsonToItemModel(itemJson, bytes) { itemModel ->
            if(stateItems.value==null) {
                stateItems.value = listOf(itemModel)
            }
            else {
                stateItems.value = stateItems.value!!.plus(itemModel)
            }
        }
    }

    private fun calculateMemoryUsage(bytes: ByteArray) {
        stateMemoryUsage.value = memoryUsageModel.calculateMemoryUsage(bytes)
    }

    private fun getUploadingHistory() {
        RoomManager.getUploadingHistory(application.applicationContext) {
            stateUploadingHistory.value = it
        }
    }

    private fun startLoginActivity() {
        application.startActivity(
            Intent(application.applicationContext, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun defaultUnauthorizedListener() {
        AccessTokenManager.deleteAccessToken(application.applicationContext)
        startLoginActivity()
    }

    private fun defaultFailureListener() {
        stateFailureRequest.value = true
    }

    fun deleteItemFromMutableStates(deletedItemId: String, deletedItemBytesSize: Int) {
        if(!isItemDeleted) {
            stateAmountItems.value --;
            stateMemoryUsage.value = memoryUsageModel.deleteFromMemoryUsage(deletedItemBytesSize)
            stateItems.value?.let {
                stateItems.value = stateItems.value!!.filter { itemModel ->
                    itemModel.id!=deletedItemId
                }
            }
            isItemDeleted = true
        }

    }
}