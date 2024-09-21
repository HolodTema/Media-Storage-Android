package com.terabyte.mediastorage.viewmodel

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.activity.LoginActivity
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.retrofit.RetrofitManager
import com.terabyte.mediastorage.util.AccessTokenManager

class PhotoInfoViewModel(private val application: Application): AndroidViewModel(application) {
    val stateItemModel = mutableStateOf<ItemModel?>(null)


    fun deleteImage(successListener: () -> Unit, failureListener: () -> Unit) {
        if(stateItemModel.value!=null) {
            RetrofitManager.deleteItem(
                application.applicationContext,
                AccessTokenManager.getAccessToken(),
                stateItemModel.value!!.id,
                successListener = {
                    successListener()
                },
                unauthorizedListener = ::defaultUnauthorizedListener,
                failureListener = failureListener
            )
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
        Toast.makeText(application.applicationContext, "Auth session expired.", Toast.LENGTH_LONG).show()
        startLoginActivity()
    }
}