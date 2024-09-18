package com.terabyte.mediastorage.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.terabyte.mediastorage.INTENT_ITEM_MODEL
import com.terabyte.mediastorage.R
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.ui.theme.Orange
import com.terabyte.mediastorage.util.ImageZoom
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

            setItemModelFromExtrasToViewModel()

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
            val textFilename = createRef()
            if(viewModel.stateItemModel.value!=null) {
                ImageZoom(
                    bitmap = viewModel.stateItemModel.value!!.image,
                    modifier = Modifier
                        .constrainAs(image) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxSize(),
                )
            }
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
                    .background(Color.Gray)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    tint = Orange,
                    contentDescription = "back",
                    modifier = Modifier
                        .size(40.dp)
                )
            }
            Text(
                text = viewModel.stateItemModel.value?.filename ?: "",
                fontSize = 20.sp,
                modifier = Modifier
                    .constrainAs(textFilename) {
                        top.linkTo(buttonBack.top)
                        bottom.linkTo(buttonBack.bottom)
                        end.linkTo(buttonBack.start)
                        start.linkTo(parent.start)
                    }
                    .padding(top = 8.dp)
                    .background(Color.Gray)
            )

        }
    }

    private fun setItemModelFromExtrasToViewModel() {
        if(intent!=null && intent.extras!=null && intent.extras!!.containsKey(INTENT_ITEM_MODEL)) {
            val itemModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.extras!!.getParcelable(INTENT_ITEM_MODEL, ItemModel::class.java)
            } else {
                intent.extras!!.getParcelable<ItemModel>(INTENT_ITEM_MODEL)
            }
            viewModel.stateItemModel.value = itemModel
        }
    }
}

