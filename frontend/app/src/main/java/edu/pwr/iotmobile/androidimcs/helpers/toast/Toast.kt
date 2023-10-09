package edu.pwr.iotmobile.androidimcs.helpers.toast

import android.content.Context
import androidx.compose.runtime.Composable

interface Toast {
    suspend fun toast(message: String)

    @Composable
    fun CollectToast(context: Context)
}