package com.terabyte.mediastorage.activity.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UploadingHistoryItem::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getUploadingHistoryDao(): UploadingHistoryDao
}