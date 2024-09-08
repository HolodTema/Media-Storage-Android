package com.terabyte.mediastorage.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthJson(
//    @Json(name = "grant_type") val grantType: String = "password",
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
//    @Json(name = "scope") val scope: String = "",
//    @Json(name = "client_id") val clientId: String = "string",
//    @Json(name = "client_secret") val secret: String = "string"
)
