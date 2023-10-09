package edu.pwr.iotmobile.androidimcs.helpers.event

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.MutableSharedFlow

class EventImpl : Event {
    private val _event = MutableSharedFlow<Any>()

    override suspend fun event(message: Any) {
        _event.emit(message)
    }

    @Composable
    override fun CollectEvent(context: Context, callback: () -> Unit) {
        LaunchedEffect(Unit) {
            _event.collect {
                callback()
            }
        }
    }
}