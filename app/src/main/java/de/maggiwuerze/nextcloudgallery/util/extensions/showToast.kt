package de.maggiwuerze.nextcloudgallery.util.extensions

import android.app.Activity
import android.os.Handler
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.pd.chocobar.ChocoBar
import de.maggiwuerze.nextcloudgallery.R

fun showToast(activity: Activity, s: String, duration: Int): Snackbar {
    return ChocoBar.builder()
        .setActivity(activity)
        .setText(s)
        .centerText()
        .setDuration(duration)
        .build().apply { show() }
}

fun showToastShort(activity: Activity, s: String) {
    showToast(activity, s, ChocoBar.LENGTH_SHORT)
}

fun showToastLong(activity: Activity, s: String) {

    showToast(activity, s, ChocoBar.LENGTH_LONG)
}

fun showToast(activity: Activity, s: String) {

    showToast(activity, s, ChocoBar.LENGTH_INDEFINITE)
}

fun Any.showToastAfterDelay(activity: Activity, s: String, delay: Long): Handler {
    return doAfterDelay({
        showToast(
            activity,
            "Login to your Nextcloud may be delayed.\nPlease allow up to 30 Seconds"
        )

    }, delay)
}