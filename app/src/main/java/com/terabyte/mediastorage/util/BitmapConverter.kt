package com.terabyte.mediastorage.util

import android.content.Context
import com.terabyte.mediastorage.json.ItemJson
import com.terabyte.mediastorage.model.ItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object BitmapConverter {

    fun itemJsonToItemModel(context: Context, itemJsonList: List<ItemJson>, listener: (List<ItemModel>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                itemJsonList.map {
                    ItemModel(
                        it.id,
                        it.ownerId,
                        it.name,
                        it.filename
                    )
                }
            }
            listener(deferred.await())
        }
    }
}