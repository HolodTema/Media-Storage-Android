package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.json.ItemJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GetAllItemsService {

    @GET("api/item")
    fun getAllItems(@Header("Authorization") accessToken: String): Call<List<ItemJson>>
}