package edu.pwr.iotmobile.androidimcs.helpers.extensions

import android.util.Log

inline fun <reified  T : Enum<T>> String.asEnum(): T? {
    return  try {
        enumValueOf<T>(this)
    } catch (e: Exception) {
        Log.e("Enum", "Enum from string", e)
        null
    }
}

fun String.firstUppercaseRestLowercase() = this.lowercase().replaceFirstChar { it.uppercase() }