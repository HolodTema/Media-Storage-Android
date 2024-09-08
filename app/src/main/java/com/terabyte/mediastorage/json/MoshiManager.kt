package com.terabyte.mediastorage.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MoshiManager {
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


}