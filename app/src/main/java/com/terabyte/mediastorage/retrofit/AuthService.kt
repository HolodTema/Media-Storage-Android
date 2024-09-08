package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.json.AuthJson
import com.terabyte.mediastorage.json.AuthResponseJson
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface AuthService {

    @FormUrlEncoded
    @POST("api/auth/login")
    @Headers(
        "accept: application/json",
        "Content-Type: application/x-www-form-urlencoded",
    )
    fun login(@Field("username") username: String, @Field("password") password: String): Call<AuthResponseJson>

}
