package edu.pwr.iotmobile.androidimcs.model.listener

import com.google.gson.Gson
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationAlertDto
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * A listener on new invitations changes.
 * The listener should be used throughout the whole app.
 */
class InvitationAlertWebSocketListener(
    client: OkHttpClient,
    onNewInvitation: (data: InvitationAlertDto) -> Unit
) {
    private val request = Request.Builder()
        .url("ws://172.20.10.8:8080/components") // Replace with your server URL and WebSocket endpoint
        .build()

    private val webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // WebSocket connection is established
            // You can send the "dashboardId" here
            webSocket.send("")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Handle incoming messages from the server
            // You will receive an infinite stream of messages here
            // Update your UI or perform any other necessary tasks
            val obj = Gson().fromJson(text, InvitationAlertDto::class.java)
            onNewInvitation(obj)
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