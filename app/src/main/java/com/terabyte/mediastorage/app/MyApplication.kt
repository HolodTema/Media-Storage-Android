package com.terabyte.mediastorage.app

import android.app.Application
import android.content.Intent
import com.terabyte.mediastorage.activity.LoginActivity
import com.terabyte.mediastorage.activity.MainActivity
import com.terabyte.mediastorage.util.DataStoreManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DataStoreManager.readFromDataStore(applicationContext, DataStoreManager.Keys.ACCESS_TOKEN) {
            if(it==null) {
                startActivity(
                    Intent(applicationContext, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
            else {
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }
}