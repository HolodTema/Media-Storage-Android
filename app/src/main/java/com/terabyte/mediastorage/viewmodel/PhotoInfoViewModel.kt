package com.terabyte.mediastorage.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.terabyte.mediastorage.model.ItemModel

class PhotoInfoViewModel(private val application: Application): AndroidViewModel(application) {
    val stateItemModel = mutableStateOf<ItemModel?>(null)
}