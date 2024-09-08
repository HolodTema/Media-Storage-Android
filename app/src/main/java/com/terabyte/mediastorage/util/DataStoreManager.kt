package com.terabyte.mediastorage.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.terabyte.mediastorage.DATA_STORE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DataStoreManager {
    val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

    fun <T> saveToDataStore(context: Context, key: Preferences.Key<T>, value: T) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {
                it[key] = value
            }
        }

    }

    fun <T> readFromDataStore(context: Context, key: Preferences.Key<T>, listener: (T?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                val preferences = context.dataStore.data.first {
                    it.contains(key)
                }
                preferences[key]
            }
            listener(deferred.await())
        }

    }

    object Keys {
        val LOGIN = stringPreferencesKey("login")
        val PASSWORD = stringPreferencesKey("password")
        val ACCESS_TOKEN = stringPreferencesKey("token")
    }
}