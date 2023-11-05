package edu.pwr.iotmobile.config

import edu.pwr.iotmobile.websocket.ComponentChangeWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig(val componentChangeWebSocketHandler: ComponentChangeWebSocketHandler)
    : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(componentChangeWebSocketHandler, "/components")
            .setAllowedOrigins("*")
    }
}