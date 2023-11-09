package edu.pwr.iotmobile.androidimcs.model.listener

import com.google.gson.Gson
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * A listener on component changes.
 * This class should be used only within DashboardViewModel.
 */
class ComponentChangeWebSocketListener(
    client: OkHttpClient,
    dashboardId: Int,
    onComponentChangeMessage: (data: ComponentListDto) -> Unit
) {
    private val request = Request.Builder()
        .url("ws://172.20.10.8:8080/components") // Replace with your server URL and WebSocket endpoint
        .build()

    private val webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // WebSocket connection is established
            // You can send the "dashboardId" here
            webSocket.send(dashboardId.toString())
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Handle incoming messages from the server
            // You will receive an infinite stream of messages here
            // Update your UI or perform any other necessary tasks
            val obj = Gson().fromJson(text, ComponentListDto::class.java)
            onComponentChangeMessage(obj)
            return
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // WebSocket connection is closed
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            // Handle connection failure
        }
    })

    fun closeWebSocket() {
        webSocket.close(1000, null)
    }
}