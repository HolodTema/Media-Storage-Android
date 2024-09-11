package com.terabyte.mediastorage.model

import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

data class ItemModel(
    val id: String,
    val ownerId: Int,
    val name: String,
    val filename: String,
    val bytes: ByteArray,
    val image: ImageBitmap,
): Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bytes.size)
        parcel.writeByteArray(bytes)

        parcel.writeString(id)
        parcel.writeInt(ownerId)
        parcel.writeString(name)
        parcel.writeString(filename)
    }

    companion object {
        @JvmField
        val CREATOR = object: Creator<ItemModel> {
            override fun createFromParcel(parcel: Parcel?): ItemModel {
                val byteArray = ByteArray(parcel!!.readInt())
                parcel.readByteArray(byteArray)
                return ItemModel(
                    parcel.readString()!!,
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!,
                    byteArray,
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
                )
            }

            override fun newArray(size: Int): Array<ItemModel> {
                return arrayOf<ItemModel>()
            }
        }
    }
}
