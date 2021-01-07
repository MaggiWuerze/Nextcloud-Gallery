package de.maggiwuerze.nextcloudgallery.util.extensions

import android.view.View

fun View.toggle() {
    var newAlpha = if (alpha == 0.0F) 1.0F else 0.0F
    animate()
        .alpha(newAlpha)
        .withEndAction {
            visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
        }
        .start()
}