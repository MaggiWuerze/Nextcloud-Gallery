package de.maggiwuerze.nextcloudgallery.util.extensions

import android.os.Handler

fun Any.doAfterDelay(function: () -> Unit, l: Long): Handler {
    return Handler().apply { postDelayed(function, l) }
}