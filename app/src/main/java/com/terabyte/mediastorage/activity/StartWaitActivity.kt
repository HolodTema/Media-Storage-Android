package com.terabyte.mediastorage.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import com.terabyte.mediastorage.activity.ui.theme.MediaStorageTheme

class StartWaitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaStorageTheme {
                Text(text = "startwait")
            }
        }
    }
}

