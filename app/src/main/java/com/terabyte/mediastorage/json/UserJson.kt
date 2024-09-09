package com.terabyte.mediastorage.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserJson(
    val email: String,
    val id: Int,
    val is_active: Boolean,
    val is_superuser: Boolean,
    val is_verified: Boolean,
    val name: String
)