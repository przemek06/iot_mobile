package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.GroupMessageDTO
import edu.pwr.iotmobile.dto.MessageDTO
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many


@Service
class NotificationService {

    private var fluxSink: Many<GroupMessageDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<GroupMessageDTO> =
        fluxSink
            .asFlux()
            .publish()

    @PostConstruct
    fun init() {
        connectableFlux.connect()
    }

    fun getInvitationFlow(userId: Int): Flux<MessageDTO> {
        return connectableFlux
            .autoConnect()
            .filter { userId in it.userIds }
            .map { it.message }
    }

    @Transactional
    fun processEntityChange(notification: GroupMessageDTO) {
        fluxSink.tryEmitNext(notification).orThrow()
    }
}
