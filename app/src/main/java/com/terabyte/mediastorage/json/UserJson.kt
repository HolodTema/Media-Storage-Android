package com.terabyte.mediastorage.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserJson(
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "is_superuser") val isSuperuser: Boolean,
    @Json(name = "is_verified") val isVerified: Boolean,
    @Json(name = "name") val name: String
)