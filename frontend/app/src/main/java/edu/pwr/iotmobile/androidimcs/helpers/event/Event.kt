package edu.pwr.iotmobile.androidimcs.helpers.event

import android.content.Context
import androidx.compose.runtime.Composable

interface Event {
    suspend fun event(message: Any)

    @Composable
    fun CollectEvent(context: Context, callback: (it: Any) -> Unit)
}