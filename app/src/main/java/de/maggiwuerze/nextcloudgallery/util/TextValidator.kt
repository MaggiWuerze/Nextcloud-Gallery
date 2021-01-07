package de.maggiwuerze.nextcloudgallery.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

abstract class TextValidator(textView: TextView) : TextWatcher {
    private val textView: TextView = textView
    abstract fun validate(textView: TextView?, text: String?)
    override fun afterTextChanged(s: Editable?) {
        val text: String = textView.text.toString()
        validate(textView, text)
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) { /* Don't care */
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) { /* Don't care */
    }

}