package com.terabyte.mediastorage.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemJson(
    @Json(name = "id") val id: String,
    @Json(name = "owner_id") val ownerId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "filename") val filename: String
)
