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
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BitmapManager {

    fun itemJsonToItemModel(itemJson: ItemJson, bytes: ByteArray, listener: (ItemModel) -> Unit) {
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

    fun addBitmapToUploadingHistoryItem(context: Context, item: UploadingHistoryItem): UploadingHistoryItem {
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

    fun getFileFromContentUri(context: Context, uri: Uri, listener: (File?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                val quality = 100

                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    if(inputStream==null) {
                        null
                    }
                    else {
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream.close()

                        val uriParts = uri.path?.split(File.separator)
                        val filename = if(uriParts==null || uriParts.isEmpty()) {
                            "image_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
                        }
                        else {
                            uriParts[uriParts.size-1]
                        }

                        val file = File("${context.externalCacheDir}/$filename.jpg")
                        val fileOutputStream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
                        fileOutputStream.close()
                        file
                    }
                }
                catch(e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            listener(deferred.await())
        }
    }
}
