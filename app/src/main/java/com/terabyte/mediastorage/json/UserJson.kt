package com.terabyte.mediastorage.json

data class UserJson(
    val id: Int,
    val name: String,
    val items: List<UserJson>,
    val password: String
)