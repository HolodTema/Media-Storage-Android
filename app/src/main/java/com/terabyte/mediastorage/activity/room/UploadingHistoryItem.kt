package com.terabyte.mediastorage.activity.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uploadingHistory")
data class UploadingHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "filename") val filename: String,
    @ColumnInfo(name = "date") val date: String
)
