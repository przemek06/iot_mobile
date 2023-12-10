package edu.pwr.iotmobile.androidimcs.model.listener

import android.util.Log
import com.google.gson.Gson
import edu.pwr.iotmobile.androidimcs.BuildConfig
import edu.pwr.iotmobile.androidimcs.data.dto.NotificationDto
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class NotificationWebSocketListener(
    client: OkHttpClient,
    private val onNotificationReceived: (data: NotificationDto) -> Unit
) {
    private val request = Request.Builder()
        .url("ws://${BuildConfig.APP_NETWORK}/notifications")
        .build()

    private val webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Notification", "onOpen WebSocket called")
            webSocket.send("")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Notification", "onNotification WebSocket called")
            Log.d("Notification", "text: $text")
            try {
                onNotificationReceived(Gson().fromJson(text, NotificationDto::class.java))
            } catch (e: Exception) {
                Log.e("WebSocket", "Could not convert fromJson.", e)
            }
            return
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Notification", "onClosed WebSocket called")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("Notification", "onFailure WebSocket called", t)
            Log.d("Notification", "response: $response")
        }
    })

    fun closeWebSocket() {
        webSocket.close(1000, null)
    }
}