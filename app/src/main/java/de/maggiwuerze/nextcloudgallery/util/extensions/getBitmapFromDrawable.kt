package de.maggiwuerze.nextcloudgallery.extensions

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap

fun Any.getBitmapFromDrawable(context: Context, res: Int): Bitmap? {
    return AppCompatResources.getDrawable(context, res)?.toBitmap()
}