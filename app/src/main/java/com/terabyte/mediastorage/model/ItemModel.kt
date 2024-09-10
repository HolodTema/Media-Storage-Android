package com.terabyte.mediastorage.model

data class ItemModel(
    val id: String,
    val ownerId: Int,
    val name: String,
    val filename: String
)
