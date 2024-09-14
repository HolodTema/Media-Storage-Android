package com.terabyte.mediastorage.activity.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UploadingHistoryDao {

    @Insert(entity = UploadingHistoryItem::class)
    fun insert(item: UploadingHistoryItem)

    @Query("select * from uploadingHistory")
    fun getAll(): List<UploadingHistoryItem>
}