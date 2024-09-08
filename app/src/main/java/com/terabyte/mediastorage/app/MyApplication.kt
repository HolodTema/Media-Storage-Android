package com.terabyte.mediastorage.app

import android.app.Application
import android.content.Intent
import com.terabyte.mediastorage.activity.MainActivity
import com.terabyte.mediastorage.util.DataStoreManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DataStoreManager.readFromDataStore(applicationContext, DataStoreManager.Keys.ACCESS_TOKEN) {
            if(it!=null) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }
}