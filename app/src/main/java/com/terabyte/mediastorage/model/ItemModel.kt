package com.terabyte.mediastorage.model

import androidx.compose.ui.graphics.ImageBitmap

data class ItemModel(
    val id: String,
    val ownerId: Int,
    val name: String,
    val filename: String,
    val image: ImageBitmap
)
