package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.json.UserJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CurrentUserService {

    @GET("api/auth/user/me")
//    @Headers(
//        "accept: application/json"
//    )
    fun getCurrentUser(@Header("Authorization") accessToken: String): Call<UserJson>
}