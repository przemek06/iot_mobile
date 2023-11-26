package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.GroupNotificationDTO
import edu.pwr.iotmobile.dto.NotificationDTO
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many


@Service
class NotificationService {

    private var fluxSink: Many<GroupNotificationDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<GroupNotificationDTO> =
        fluxSink
            .asFlux()
            .publish()

    @PostConstruct
    fun init() {
        connectableFlux.connect()
    }

    fun getInvitationFlow(userId: Int): Flux<NotificationDTO> {
        return connectableFlux
            .autoConnect()
            .filter { userId in it.userIds }
            .map { it.message }
    }

    @Transactional
    fun processEntityChange(notification: GroupNotificationDTO) {
        fluxSink.tryEmitNext(notification).orThrow()
    }
}
