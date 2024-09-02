package com.terabyte.mediastorage.retrofit

import com.terabyte.mediastorage.BASE_URL
import retrofit2.Retrofit

object RetrofitManager {
    private var client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()


}