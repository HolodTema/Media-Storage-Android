package com.terabyte.mediastorage.util

import com.terabyte.mediastorage.model.ItemModel

object ActivityCommunicationManager {
    var itemModel: ItemModel? = null
        private set

    fun putItemModel(itemModel: ItemModel) {
        this.itemModel = itemModel
    }

    fun dropItemModel() {
        this.itemModel = null
    }
}