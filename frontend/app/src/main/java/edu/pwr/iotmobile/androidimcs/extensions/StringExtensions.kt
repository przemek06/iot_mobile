package edu.pwr.iotmobile.androidimcs.extensions

import android.util.Log

inline fun <reified  T : Enum<T>> String.asEnum(): T? {
    return  try {
        enumValueOf<T>(this)
    } catch (e: Exception) {
        Log.e("Enum", "Enum from string", e)
        null
    }
}