package edu.pwr.iotmobile.config

import edu.pwr.iotmobile.websocket.*
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig(
    val componentChangeWebSocketHandler: ComponentChangeWebSocketHandler,
    val invitationAlertWebSocketHandler: InvitationAlertWebSocketHandler,
    val projectDeletedWebSocketHandler: ProjectDeletedWebSocketHandler,
    val incomingMessageWebSocketHandler: IncomingMessageWebSocketHandler,
    val deviceIncomingMessageWebSocketHandler: DeviceIncomingMessageWebSocketHandler,
    val notificationWebSocketHandler: NotificationWebSocketHandler
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(componentChangeWebSocketHandler, "/components")
            .setAllowedOrigins("*")
        registry
            .addHandler(invitationAlertWebSocketHandler, "/invitations")
            .setAllowedOrigins("*")
        registry
            .addHandler(projectDeletedWebSocketHandler, "/projectDeleted")
            .setAllowedOrigins("*")
        registry
            .addHandler(incomingMessageWebSocketHandler, "/messages")
            .setAllowedOrigins("*")
        registry
            .addHandler(deviceIncomingMessageWebSocketHandler, "/messages/device")
            .setAllowedOrigins("*")
        registry
            .addHandler(notificationWebSocketHandler, "/notifications")
            .setAllowedOrigins("*")
    }
}