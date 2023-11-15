package edu.pwr.iotmobile.config

import edu.pwr.iotmobile.websocket.ComponentChangeWebSocketHandler
import edu.pwr.iotmobile.websocket.IncomingMessageWebSocketHandler
import edu.pwr.iotmobile.websocket.InvitationAlertWebSocketHandler
import edu.pwr.iotmobile.websocket.ProjectDeletedWebSocketHandler
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
        val incomingMessageWebSocketHandler: IncomingMessageWebSocketHandler
)
    : WebSocketConfigurer {
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
    }
}