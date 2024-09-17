package com.terabyte.mediastorage.activity.room

import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "uploadingHistory")
data class UploadingHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "filename") val filename: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "imageUri") val imageUri: String,
    @Ignore val image: ImageBitmap? = null
) {
    constructor(id: Long, filename: String, date: String, imageUri: String): this(id, filename, date, imageUri, null)
}
