package com.terabyte.mediastorage.util

import android.content.Context

object AccessTokenManager {
    private var accessToken = ""


    fun deleteAccessToken(context: Context) {
        DataStoreManager.deleteFromDataStore(context, DataStoreManager.Keys.ACCESS_TOKEN)
        accessToken = ""
    }


    fun getAccessToken() = accessToken


    fun saveAccessToken(context: Context, token: String) {
        accessToken = token
        DataStoreManager.saveToDataStore(context, DataStoreManager.Keys.ACCESS_TOKEN, token)
    }


    //this method checks if there is a token in DataStore
    //and if token is in DataStore, the method saves it to the variable accessToken
    //if token is NOT in DataStore, the method returns false in listener
    fun cacheIfTokenExists(context: Context, listener: (Boolean) -> Unit) {
        DataStoreManager.readFromDataStore(context, DataStoreManager.Keys.ACCESS_TOKEN) {
            if(it!=null) {
                accessToken = it
                listener(true)
            }
            else {
                listener(false)
            }
        }
    }
}