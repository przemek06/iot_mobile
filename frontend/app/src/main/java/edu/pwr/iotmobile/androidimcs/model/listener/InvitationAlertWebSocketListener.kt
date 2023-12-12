package edu.pwr.iotmobile.androidimcs.model.listener

import android.util.Log
import edu.pwr.iotmobile.androidimcs.BuildConfig
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
    onNewInvitation: (data: Boolean) -> Unit
) {
    private val request = Request.Builder()
        .url("ws://${BuildConfig.APP_NETWORK}/invitations")
        .build()

    private val webSocket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Invitation", "onOpen WebSocket called")
            // WebSocket connection is established
            webSocket.send("")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Invitation", "onMessage WebSocket called")
            Log.d("Invitation", "text: $text")
            // Handle incoming messages from the serve
            onNewInvitation(text.toBoolean())
            return
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // WebSocket connection is closed
            Log.d("Invitation", "onClosed WebSocket called")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            // Handle connection failure
            Log.e("Invitation", "onFailure WebSocket called", t)
            Log.d("Invitation", "response: $response")
        }
    })

    fun closeWebSocket() {
        webSocket.close(1000, null)
    }
}