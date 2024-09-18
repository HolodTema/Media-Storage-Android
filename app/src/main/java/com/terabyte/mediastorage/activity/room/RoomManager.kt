package com.terabyte.mediastorage.activity.room

import android.content.Context
import androidx.room.Room
import com.terabyte.mediastorage.ROOM_DATABASE_NAME
import com.terabyte.mediastorage.util.BitmapManager
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

    fun getUploadingHistory(context: Context, listener: (List<UploadingHistoryItem>) -> Unit) {
        checkInitDatabase(context)
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                val list = db.getUploadingHistoryDao().getAll()
                list.map { item ->
                    BitmapManager.addBitmapToUploadingHistoryItem(context, item)
                }
                list
            }
            listener(deferred.await())
        }
    }


    fun insertUploadingHistoryItem(context: Context, item: UploadingHistoryItem) {
        checkInitDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            db.getUploadingHistoryDao().insert(item)
        }
    }
}