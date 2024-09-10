package com.terabyte.mediastorage.util

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import com.terabyte.mediastorage.json.ItemJson
import com.terabyte.mediastorage.model.ItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object BitmapConverter {

    fun itemJsonToItemModel(context: Context, itemJson: ItemJson, bytes: ByteArray, listener: (ItemModel) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                ItemModel(
                    itemJson.id,
                    itemJson.ownerId,
                    itemJson.name,
                    itemJson.filename,
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
                )
            }
            listener(deferred.await())
        }
    }
}