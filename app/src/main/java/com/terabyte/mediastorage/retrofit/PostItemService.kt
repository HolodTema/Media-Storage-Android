package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.json.ItemJson
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface PostItemService {

    @Streaming
    @POST("api/item/{item_id}")
    fun postItem(@Header("Authorization") accessToken: String, @Query("name") name: String, @Query("owner_id") ownerId: Int): Call<ItemJson>
}