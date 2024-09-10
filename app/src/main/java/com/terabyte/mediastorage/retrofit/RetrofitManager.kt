package com.terabyte.mediastorage.retrofit

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.terabyte.mediastorage.BASE_URL
import com.terabyte.mediastorage.TOKEN_TYPE
import com.terabyte.mediastorage.json.AuthResponseJson
import com.terabyte.mediastorage.json.ItemJson
import com.terabyte.mediastorage.json.MoshiManager
import com.terabyte.mediastorage.json.UserJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitManager {
    private lateinit var client: Retrofit


    fun auth(context: Context, username: String, password: String, successListener: (AuthResponseJson) -> Unit, incorrectLoginListener: () -> Unit, failureListener: () -> Unit) {
        if(!::client.isInitialized) createClient(context)

        val service = client.create(AuthService::class.java)
        val call = service.login(username, password)
        call.enqueue(
            object: Callback<AuthResponseJson> {
                override fun onResponse(p0: Call<AuthResponseJson>, p1: Response<AuthResponseJson>) {
                    if(p1.body()==null) {
                        if(p1.code()==400) incorrectLoginListener()
                        else failureListener()
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

    fun logout(context: Context, accessToken: String, successListener: () -> Unit, failureListener: () -> Unit) {
        if(!::client.isInitialized) createClient(context)

        val service = client.create(LogoutService::class.java)
        val call = service.login("$TOKEN_TYPE $accessToken")
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

    fun getCurrentUser(context: Context, accessToken: String, successListener: (UserJson) -> Unit, unauthorizedListener: () -> Unit, failureListener: () -> Unit) {
        if(!::client.isInitialized) createClient(context)

        val service = client.create(CurrentUserService::class.java)
        val call = service.getCurrentUser("$TOKEN_TYPE $accessToken")
        call.enqueue(object: Callback<UserJson> {
            override fun onResponse(p0: Call<UserJson>, p1: Response<UserJson>) {
                if(p1.body()==null) {
                    if(p1.code()==401) unauthorizedListener()
                    else failureListener()
                }
                else successListener(p1.body()!!)
            }

            override fun onFailure(p0: Call<UserJson>, p1: Throwable) {
                failureListener()
            }
        })
    }


    fun getAllItems(context: Context, accessToken: String, successListener: (List<ItemJson>) -> Unit, unauthorizedListener: () -> Unit, failureListener: () -> Unit) {
        if(!::client.isInitialized) createClient(context)

        val service = client.create(GetAllItemsService::class.java)
        val call = service.getAllItems("$TOKEN_TYPE $accessToken")
        call.enqueue(
            object: Callback<List<ItemJson>> {
                override fun onResponse(p0: Call<List<ItemJson>>, p1: Response<List<ItemJson>>) {
                    if(p1.body()==null) {
                        if(p1.code()==401) unauthorizedListener()
                        else failureListener()
                    }
                    else successListener(p1.body()!!)
                }

                override fun onFailure(p0: Call<List<ItemJson>>, p1: Throwable) {
                    failureListener()
                }
            }
        )
    }

    fun getItem(context: Context, accessToken: String, itemId: String, successListener: (ByteArray) -> Unit, unauthorizedListener: () -> Unit, failureListener: () -> Unit) {
        if(!::client.isInitialized) createClient(context)

        val service = client.create(GetItemService::class.java)
        val call = service.getAllItems("$TOKEN_TYPE $accessToken", itemId)
        call.enqueue(
            object: Callback<ResponseBody> {
                override fun onResponse(p0: Call<ResponseBody>, p1: Response<ResponseBody>) {
                    if(p1.body()==null) {
                        if(p1.code()==401) unauthorizedListener()
                        else failureListener()
                    }
                    else {
                        CoroutineScope(Dispatchers.Main).launch {
                            val deferred = async(Dispatchers.IO) {
                                p1.body()!!.bytes()
                            }
                            successListener(deferred.await())
                        }
                    }
                }

                override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                    failureListener()
                }
            }
        )
    }

    private fun createClient(context: Context) {
        client = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(MoshiManager.moshi))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(context))
                    .build()
            )
            .build()
    }

}