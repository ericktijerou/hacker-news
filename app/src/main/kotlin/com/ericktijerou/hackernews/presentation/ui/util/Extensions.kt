package com.ericktijerou.hackernews.presentation.ui.util

import android.app.Activity
import android.content.Intent
import android.text.Spanned
import android.text.format.DateUtils
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

fun <T> Activity.startNewActivity(
    target: KClass<T>,
    extras: Intent.() -> Unit = { }
) where T : Activity {
    val intent = Intent(this, target.java)
    intent.apply { extras() }
    this.startActivity(intent)
}

fun Activity.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(resId), duration).show()
}

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, body: (T) -> Unit) {
    this.observe(lifecycleOwner, Observer { body(it) })
}

fun String.toSpanned(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun String.getRelativeTime(): String {
    val now = Date().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ROOT)
    val date = dateFormat.parse(this)
    val timeInMilliseconds = date?.time ?: 0
    val difference = now - timeInMilliseconds
    val relativeTime = when {
        difference < DateUtils.MINUTE_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
            timeInMilliseconds,
            now,
            DateUtils.SECOND_IN_MILLIS
        )
        difference < DateUtils.HOUR_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
            timeInMilliseconds,
            now,
            DateUtils.MINUTE_IN_MILLIS
        )
        difference < DateUtils.DAY_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
            timeInMilliseconds,
            now,
            DateUtils.HOUR_IN_MILLIS
        )
        difference < DateUtils.WEEK_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
            timeInMilliseconds,
            now,
            DateUtils.DAY_IN_MILLIS
        )
        else -> DateUtils.getRelativeTimeSpanString(
            timeInMilliseconds,
            now,
            DateUtils.WEEK_IN_MILLIS
        )
    }
    return relativeTime.toString()
}