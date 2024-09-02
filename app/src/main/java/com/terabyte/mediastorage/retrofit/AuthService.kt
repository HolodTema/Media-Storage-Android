package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.json.AuthJson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthService {

    @POST("api/auth/login")
    fun login(@Header("Authorization: Bearer ") token: String, @Body authJson: AuthJson): Call<Unit>

}
