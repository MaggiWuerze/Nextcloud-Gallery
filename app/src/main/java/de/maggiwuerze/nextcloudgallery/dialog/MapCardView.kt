package de.maggiwuerze.nextcloudgallery.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.cardview.widget.CardView

class MapCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

}