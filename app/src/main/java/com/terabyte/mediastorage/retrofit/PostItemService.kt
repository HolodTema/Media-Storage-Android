package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.json.ItemJson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PostItemService {

    @Multipart
    @Headers(
        "accept: application/json",
    )
    @POST("api/item")
    fun postItem(@Header("Authorization") accessToken: String, @Query("name") name: String, @Query("owner_id") ownerId: Int, @Part part: MultipartBody.Part): Call<ItemJson>
}