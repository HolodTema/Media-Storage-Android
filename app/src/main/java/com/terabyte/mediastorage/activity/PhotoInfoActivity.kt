package com.terabyte.mediastorage.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.terabyte.mediastorage.INTENT_DELETED_ITEM_BYTES_SIZE
import com.terabyte.mediastorage.INTENT_DELETED_ITEM_ID
import com.terabyte.mediastorage.INTENT_ITEM_MODEL
import com.terabyte.mediastorage.model.ItemModel
import com.terabyte.mediastorage.ui.theme.MediaStorageTheme
import com.terabyte.mediastorage.util.ImageZoom
import com.terabyte.mediastorage.viewmodel.PhotoInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PhotoInfoActivity : ComponentActivity() {
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
    fun ButtonBack(
        modifier: Modifier,
        composition: LottieComposition?,
        isPlaying: MutableState<Boolean>
    ) {
        IconButton(
            onClick = {
                isPlaying.value = true
                CoroutineScope(Dispatchers.Main).launch {
                    val deferred = async(Dispatchers.IO) {
                        delay(400L)
                    }
                    deferred.await()
                    finish()
                }
            },
            modifier = modifier
                .size(35.dp)
        ) {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                composition = composition,
                isPlaying = isPlaying.value,
                speed = 0.4f,
            )
        }
    }

    @Composable
    fun TextFilename(modifier: Modifier) {
        var text = viewModel.stateItemModel.value?.filename ?: ""
        if(text.length>40) {
            text = text.substring(37) + "..."
        }
        Text(
            text = text,
            fontSize = 20.sp,
            modifier = modifier
        )
    }

    @Composable
    fun ButtonDeleteImage(
        modifier: Modifier,
        composition: LottieComposition?,
        isPlaying: MutableState<Boolean>,
    ) {

        IconButton(
            onClick = {
                isPlaying.value = true

                CoroutineScope(Dispatchers.Main).launch {
                    val deferred = async(Dispatchers.IO) {
                        delay(1000L)
                    }
                    deferred.await()
                    viewModel.deleteImage(
                        successListener = {
                            startMainActivityWithDeletedInfoExtras()
                        },
                        failureListener = {
                            Toast.makeText(
                                this@PhotoInfoActivity,
                                "Something went wrong. Check your Internet connection.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            },
            modifier = modifier
                .size(80.dp)
        ) {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                composition = composition,
                isPlaying = isPlaying.value,
                speed = 1f,
            )
        }
    }

    @Composable
    fun PhotoInfoContent() {
        val buttonDeleteComposition = rememberLottieComposition(
            spec = LottieCompositionSpec.Asset("ic_lottie_delete.json")
        )
        val buttonDeleteIsPlaying = remember {
            mutableStateOf(false)
        }
        val buttonBackComposition = rememberLottieComposition(
            spec = LottieCompositionSpec.Asset("ic_lottie_back.json")
        )
        val buttonBackIsPlaying = remember {
            mutableStateOf(false)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val image = createRef()
            val buttonBack = createRef()
            val buttonDelete = createRef()
            val textFilename = createRef()

            ImageZoom(
                bitmap = viewModel.stateItemModel.value!!.image,
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            TextFilename(
                modifier = Modifier
                    .constrainAs(textFilename) {
                        top.linkTo(buttonBack.top)
                        bottom.linkTo(buttonBack.bottom)
                        end.linkTo(buttonDelete.start)
                        start.linkTo(buttonBack.end)
                    }
                    .padding(top = 8.dp)
            )

            ButtonBack(
                modifier = Modifier
                    .constrainAs(buttonBack) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(top = 16.dp, start = 18.dp),
                composition = buttonBackComposition.value,
                isPlaying = buttonBackIsPlaying
            )

            ButtonDeleteImage(
                modifier = Modifier
                    .constrainAs(buttonDelete) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .offset(0.dp, -20.dp),
                composition = buttonDeleteComposition.value,
                isPlaying = buttonDeleteIsPlaying,
            )
        }
    }

    private fun setItemModelFromExtrasToViewModel() {
        if (intent != null && intent.extras != null && intent.extras!!.containsKey(
                INTENT_ITEM_MODEL
            )
        ) {
            val itemModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.extras!!.getParcelable(INTENT_ITEM_MODEL, ItemModel::class.java)
            } else {
                intent.extras!!.getParcelable<ItemModel>(INTENT_ITEM_MODEL)
            }
            viewModel.stateItemModel.value = itemModel
        }
    }

    private fun startMainActivityWithDeletedInfoExtras() {
        val intent = Intent(this@PhotoInfoActivity, MainActivity::class.java)
        viewModel.stateItemModel.value?.let {
            intent.putExtra(
                INTENT_DELETED_ITEM_ID,
                viewModel.stateItemModel.value!!.id
            )
            intent.putExtra(
                INTENT_DELETED_ITEM_BYTES_SIZE,
                viewModel.stateItemModel.value!!.bytes.size
            )
        }
        startActivity(intent)
        finish()
    }
}








