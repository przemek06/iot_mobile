package edu.pwr.iotmobile.androidimcs.helpers.toast

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.MutableSharedFlow

class ToastImpl : edu.pwr.iotmobile.androidimcs.helpers.toast.Toast {
    private val _toastMessage = MutableSharedFlow<String>()

    override suspend fun toast(message: String) {
        _toastMessage.emit(message)
    }

    @Composable
    override fun CollectToast(context: Context) {
        LaunchedEffect(Unit) {
            _toastMessage
                .collect { message ->
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
    }

}