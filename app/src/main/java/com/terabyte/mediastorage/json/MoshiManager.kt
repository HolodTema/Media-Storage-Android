package com.terabyte.mediastorage.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MoshiManager {
    private val moshi = Moshi.Builder()
        .build()

    fun convertAuthToJson(auth: AuthJson, listener: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val adapter: JsonAdapter<AuthJson> = moshi.adapter(AuthJson::class.java)
            listener(adapter.toJson(auth))
        }
    }
}