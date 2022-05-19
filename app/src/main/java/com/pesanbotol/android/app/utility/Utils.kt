package com.pesanbotol.android.app.utility

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Environment
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pesanbotol.android.app.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun getColoredSpanned(text: String, color: String): String? {
            return "<font color=$color>$text</font>"
        }

        fun setTextColor(
            context: Context,
            tv: TextView,
            startPosition: Int,
            endPosition: Int
        ) {
            val spannableStr = SpannableString(tv.text)

            val underlineSpan = UnderlineSpan()
            spannableStr.setSpan(
                underlineSpan,
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )

            val backgroundColorSpan = ForegroundColorSpan(
                ContextCompat.getColor(
                    context,
                    R.color.blue_200
                )
            )
            spannableStr.setSpan(
                backgroundColorSpan,
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )

            val styleSpanItalic = StyleSpan(Typeface.BOLD)
            spannableStr.setSpan(
                styleSpanItalic,
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )

            tv.text = spannableStr
        }
    }
}