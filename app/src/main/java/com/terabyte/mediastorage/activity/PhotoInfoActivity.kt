package com.terabyte.mediastorage.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.terabyte.mediastorage.R
import com.terabyte.mediastorage.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.ui.theme.Orange
import com.terabyte.mediastorage.viewmodel.PhotoInfoViewModel

class PhotoInfoActivity: ComponentActivity() {
    private lateinit var viewModel: PhotoInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )[PhotoInfoViewModel::class.java]
            MediaStorageTheme {
                PhotoInfoContent()
            }
        }
    }

    @Composable
    fun PhotoInfoContent() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val image = createRef()
            val buttonBack = createRef()
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize(),
                contentDescription = "photo"
            )
            IconButton(
                onClick = {
                    finish()
                },
                modifier = Modifier
                    .constrainAs(buttonBack) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(top = 8.dp, end = 8.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    tint = Orange,
                    contentDescription = "back",
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }
    }
}

