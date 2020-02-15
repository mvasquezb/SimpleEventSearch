@file:JvmName("ExtensionUtils")
package com.pmvb.simpleeventsearch.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.androidnetworking.common.RequestBuilder
import java.text.SimpleDateFormat
import java.util.*

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T : RequestBuilder> T.addQueryIf(condition: Boolean, key: String, value: String?): T {
    if (condition) {
        addQueryParameter(key, value)
    }
    return this
}

fun Date.format(format: String): String {
    return this.format(format, Locale.getDefault())
}

fun Date.format(format: String, locale: Locale): String {
    return SimpleDateFormat(format, locale).format(this)
}

fun String.toDate(): Date {
    val format = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    return this.toDate(format, Locale.getDefault())
}

fun String.toDate(format: String): Date {
    return this.toDate(format, Locale.getDefault())
}

fun String.toDate(format: String, locale: Locale): Date {
    return SimpleDateFormat(format, locale).parse(this)
}