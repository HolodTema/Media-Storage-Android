package com.terabyte.mediastorage.activity.room

import android.content.Context
import androidx.room.Room
import com.terabyte.mediastorage.ROOM_DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object RoomManager {
    private lateinit var db: AppDatabase

    private fun checkInitDatabase(context: Context) {
        if(!::db.isInitialized) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, ROOM_DATABASE_NAME).build()
        }
    }

    private fun getUploadingHistory(context: Context, listener: (List<UploadingHistoryItem>) -> Unit) {
        checkInitDatabase(context)
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                db.getUploadingHistoryDao().getAll()
            }
            listener(deferred.await())
        }
    }

    private fun insertUploadingHistoryItem(context: Context, item: UploadingHistoryItem) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getUploadingHistoryDao().insert(item)
        }
    }
}