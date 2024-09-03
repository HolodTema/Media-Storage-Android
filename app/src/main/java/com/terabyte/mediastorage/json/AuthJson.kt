package com.terabyte.mediastorage.json

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AuthJson(
    val username: String,
    val password: String
)
