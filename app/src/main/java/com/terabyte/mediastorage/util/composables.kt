package com.terabyte.mediastorage.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer


@Composable
fun ImageZoom(
    modifier: Modifier,
    bitmap: ImageBitmap
) {
    val MAX_ZOOM = 4f
    val MIN_ZOOM = 1f
    val scale = remember {
        mutableFloatStateOf(1f)
    }
    val offset = remember {
        mutableStateOf(Offset.Zero)
    }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        if(scale.floatValue*zoomChange in MIN_ZOOM..MAX_ZOOM) scale.floatValue *=zoomChange
//        offset.value += offsetChange

    }

    Box(
        modifier = Modifier
            .then(modifier)
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = "image",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .graphicsLayer {
                    scaleX = scale.floatValue
                    scaleY = scale.floatValue
                    translationX = offset.value.x
                    translationY = offset.value.y
                    Log.d("myDebug", "${offset.value.x}  ${offset.value.y}")
                }
                .transformable(state)

        )
    }
}