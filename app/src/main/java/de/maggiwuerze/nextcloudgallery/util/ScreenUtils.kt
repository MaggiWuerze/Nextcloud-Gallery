package de.maggiwuerze.nextcloudgallery.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

/**
 * Provides utility methods for working with the device screen.
 */
object ScreenUtils {

    /**
     * Converts the given device independent pixels (DIP) value into the corresponding pixels
     * value for the current screen.
     *
     * @param context Context instance
     * @param dip The DIP value to convert
     *
     * @return The pixels value for the current screen of the given DIP value.
     */
    fun convertDIPToPixels(context: Context, dip: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), displayMetrics)
            .toInt()
    }

    /**
     * Converts the given device independent pixels (DIP) value into the corresponding pixels
     * value for the current screen.
     *
     * @param context Context instance
     * @param dip The DIP value to convert
     *
     * @return The pixels value for the current screen of the given DIP value.
     */
    fun convertDIPToPixels(context: Context, dip: Float): Int {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics).toInt()
    }

    /**
     * Converts the given pixels value into the corresponding device independent pixels (DIP)
     * value for the current screen.
     *
     * @param context Context instance
     * @param pixels The pixels value to convert
     *
     * @return The DIP value for the current screen of the given pixels value.
     */
    fun convertPixelsToDIP(context: Context, pixels: Int): Float {
        val displayMetrics = context.resources.displayMetrics
        return pixels / (displayMetrics.densityDpi / 160f)
    }

    /**
     * Returns the current screen dimensions in device independent pixels (DIP) as a [Point] object where
     * [Point.x] is the screen width and [Point.y] is the screen height.
     *
     * @param context Context instance
     *
     * @return The current screen dimensions in DIP.
     */
    fun getScreenDimensionsInDIP(context: Context): Point {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val configuration = context.resources.configuration
            return Point(configuration.screenWidthDp, configuration.screenHeightDp)

        } else {
            // APIs prior to v13 gave the screen dimensions in pixels. We convert them to DIPs before returning them.
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            val screenWidthInDIP = convertPixelsToDIP(context, displayMetrics.widthPixels).toInt()
            val screenHeightInDIP = convertPixelsToDIP(context, displayMetrics.heightPixels).toInt()
            return Point(screenWidthInDIP, screenHeightInDIP)
        }
    }

    /**
     * @param context Context instance
     *
     * @return [true] if the device is in landscape orientation, [false] otherwise.
     */
    fun isInLandscapeOrientation(context: Context): Boolean {
        val configuration = context.resources.configuration
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * @param context Context instance
     *
     * @return [true] if the device has a small screen, [false] otherwise.
     */
    fun hasSmallScreen(context: Context): Boolean {
        return getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_SMALL
    }

    /**
     * @param context Context instance
     *
     * @return [true] if the device has a normal screen, [false] otherwise.
     */
    fun hasNormalScreen(context: Context): Boolean {
        return getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_NORMAL
    }

    /**
     * @param context Context instance
     *
     * @return [true] if the device has a large screen, [false] otherwise.
     */
    fun hasLargeScreen(context: Context): Boolean {
        return getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * @param context Context instance
     *
     * @return [true] if the device has an extra large screen, [false] otherwise.
     */
    fun hasXLargeScreen(context: Context): Boolean {
        return getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

    /**
     * The size of the screen, one of 4 possible values:
     *
     *
     *  * http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_SMALL
     *  * http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_NORMAL
     *  * http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_LARGE
     *  * http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_XLARGE
     *
     *
     * See http://developer.android.com/reference/android/content/res/Configuration.html#screenLayout for more details.
     *
     * @param context Context instance
     *
     * @return The size of the screen
     */
    fun getScreenSize(context: Context): Int {
        val configuration = context.resources.configuration
        return configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
    }

}