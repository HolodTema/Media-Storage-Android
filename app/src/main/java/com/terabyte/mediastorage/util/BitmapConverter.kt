package com.terabyte.mediastorage.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.graphics.asImageBitmap
import com.terabyte.mediastorage.activity.room.UploadingHistoryItem
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
                    bytes,
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
                )
            }
            listener(deferred.await())
        }
    }

    suspend fun addBitmapToUploadingHistoryItem(context: Context, item: UploadingHistoryItem): UploadingHistoryItem {
        try {
            val uri = Uri.parse(item.imageUri)
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }.asImageBitmap()
            return item.copy(
                image = bitmap
            )

        } catch(e: Exception) {
            return item
        }
    }

    suspend fun bitmapFromLocalUri(context: Context, uri: Uri): Bitmap? {
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            return bitmap
        } catch(e: Exception) {
            return null
        }
    }
}