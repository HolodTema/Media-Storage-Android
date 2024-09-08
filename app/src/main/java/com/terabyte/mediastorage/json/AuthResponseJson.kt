package com.terabyte.mediastorage.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponseJson(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String
)
