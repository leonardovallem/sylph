package com.vallem.sylph.shared.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.ImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlin.math.max
import kotlin.math.min

@Composable
fun ImageCropper(imageCropper: ImageCropper) {
    LaunchedEffect(imageCropper.cropState) {
        imageCropper.cropState?.let {
            it.region = it.region.setSquareAspectRatio()
            it.aspectLock = true
        }
    }

    imageCropper.cropState?.let {
        ImageCropperDialog(state = it) {}
    }
}

fun CropResult.getOrNull() = (this as? CropResult.Success)?.bitmap

internal fun Rect.setSquareAspectRatio(): Rect {
    val dim = max(width, height)
    return Rect(Offset.Zero, Size(dim, height = dim))
        .fitIn(this)
        .centerIn(this)
}

internal fun Rect.fitIn(outer: Rect): Rect {
    val scaleF = min(outer.width / width, outer.height / height)
    return scale(scaleF, scaleF)
}

internal fun Rect.scale(sx: Float, sy: Float) = setSizeTL(width = width * sx, height = height * sy)

internal fun Rect.setSizeTL(width: Float, height: Float) =
    Rect(offset = topLeft, size = Size(width, height))

internal fun Rect.centerIn(outer: Rect): Rect =
    translate(outer.center.x - center.x, outer.center.y - center.y)
