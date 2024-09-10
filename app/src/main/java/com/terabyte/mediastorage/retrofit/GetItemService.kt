package com.terabyte.mediastorage.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Streaming

interface GetItemService {

    @Streaming
    @GET("api/item/{item_id}")
    fun getAllItems(@Header("Authorization") accessToken: String, @Path("item_id") itemId: String): Call<ResponseBody>
}