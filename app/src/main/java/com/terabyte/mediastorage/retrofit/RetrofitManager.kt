package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.BASE_URL
import com.terabyte.mediastorage.json.AuthJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

object RetrofitManager {
    private var client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()


    fun auth(token: String, username: String, password: String, successListener: () -> Unit, failureListener: () -> Unit) {
        val authJson = AuthJson(username, password)
        val service = client.create(AuthService::class.java)
        val call = service.login(token, authJson)
        call.enqueue(
            object: Callback<Unit> {
                override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {
                    successListener()
                }

                override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                    failureListener()
                }
            }
        )
    }

}