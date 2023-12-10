package edu.pwr.iotmobile.androidimcs.model.listener

import android.util.Log
import com.google.gson.Gson
import edu.pwr.iotmobile.androidimcs.BuildConfig
import edu.pwr.iotmobile.androidimcs.data.dto.MessageDto
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class MessageReceivedWebSocketListener(
    client: OkHttpClient,
    topicNames: List<String>,
    onMessageReceived: (data: MessageDto) -> Unit
) {
    private val request = Request.Builder()
        .url("ws://${BuildConfig.APP_NETWORK}/messages") // Replace with your server URL and WebSocket endpoint
        .build()

    private val webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // WebSocket connection is established
            // You can send the "dashboardId" here
            Log.d("mess", "opened webSocket")
            Log.d("mess", "sending: " + topicNames.distinct().joinToString(","))
            webSocket.send(topicNames.distinct().joinToString(","))
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Handle incoming messages from the server
            // You will receive an infinite stream of messages here
            // Update your UI or perform any other necessary tasks
            Log.d("WebSocket", "Message received")
            Log.d("WebSocket", text)
            try {
                val obj = Gson().fromJson(text, MessageDto::class.java)
                onMessageReceived(obj)
            } catch (e: Exception) {
                Log.e("WebSocket", "Could not convert fromJson.", e)
            }
            return
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // WebSocket connection is closed
            Log.d("WebSocket", "onClosed called")
            Log.d("WebSocket", reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            // Handle connection failure
            Log.d("WebSocket", "onFailure called")
            Log.d("WebSocket", response.toString())
            Log.e("WebSocket", "Error", t)
        }
    })

    fun closeWebSocket() {
        webSocket.close(1000, null)
    }
}