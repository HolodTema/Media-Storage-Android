package com.terabyte.mediastorage.retrofit

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.terabyte.mediastorage.BASE_URL
import com.terabyte.mediastorage.json.AuthJson
import com.terabyte.mediastorage.json.AuthResponseJson
import com.terabyte.mediastorage.json.MoshiManager
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitManager {
    private lateinit var client: Retrofit


    fun auth(context: Context, username: String, password: String, successListener: (AuthResponseJson) -> Unit, failureListener: () -> Unit) {
        if(!::client.isInitialized) createClient(context)

        val service = client.create(AuthService::class.java)
        val call = service.login(username, password)
        call.enqueue(
            object: Callback<AuthResponseJson> {
                override fun onResponse(p0: Call<AuthResponseJson>, p1: Response<AuthResponseJson>) {
                    if(p1.body()==null) {
                        failureListener()
                    }
                    else {
                        successListener(p1.body()!!)
                    }
                }

                override fun onFailure(p0: Call<AuthResponseJson>, p1: Throwable) {
                    failureListener()
                }
            }
        )
    }

    private fun createClient(context: Context) {
        client = Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .addConverterFactory(MoshiConverterFactory.create(MoshiManager.moshi))
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .setPrettyPrinting()
                    .create()
            ))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(context))
                    .build()
            )
            .build()
    }

}