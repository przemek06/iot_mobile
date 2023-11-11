package edu.pwr.iotmobile.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.service.*
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import reactor.core.Disposable

class ProjectDeletedWebSocketHandler(
        private val projectDeletedNotificationService: ProjectDeletedNotificationService,
        val userService: UserService,
        val projectService: ProjectService
) : TextWebSocketHandler() {
    private val objectMapper = ObjectMapper()

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val projectId = message.payload.toInt()
        val userId = userService.findUserByEmail(session.principal?.name ?: throw NoAuthenticationException())?.id
                ?: throw NoAuthenticationException()

        if (!projectService.isInProject(userId, projectId))
            throw NotAllowedException()

        val subscription = projectDeletedNotificationService
                .getProjectDeletedFlow(userId, projectId)
                .subscribe {
                    if (session.isOpen) {
                        val responseMessage = objectMapper.writeValueAsString(it)
                        session.sendMessage(TextMessage(responseMessage))
                    }
                }

        session.attributes["subscription"] = subscription
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val subscription = session.attributes["subscription"]
        if (subscription != null && subscription is Disposable) {
            subscription.dispose()
        }
        session.close()
        super.afterConnectionClosed(session, status)
    }
}
