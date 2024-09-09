package com.terabyte.mediastorage.retrofit

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST


interface LogoutService {

    @POST("api/auth/logout")
    fun login(@Header("Authorization") accessToken: String): Call<Unit>

}
