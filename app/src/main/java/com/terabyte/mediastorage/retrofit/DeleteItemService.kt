package com.terabyte.mediastorage.retrofit

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface DeleteItemService {

    @Headers(
        "accept: */*",
    )
    @DELETE("api/item/{item_id}")
    fun deleteItem(@Header("Authorization") accessToken: String, @Path("item_id") itemId: String): Call<Unit>
}