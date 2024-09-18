package com.terabyte.mediastorage.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickImageActivityResultContract: ActivityResultContract<Unit, Uri?>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        val intentGallery = Intent(Intent.ACTION_GET_CONTENT)
        intentGallery.type = "image/*"
        return intentGallery
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if(intent!=null) {
            return intent.data
        }
        else {
            return null
        }
    }
}