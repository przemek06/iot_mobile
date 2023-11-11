package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many


@Service
class InvitationAlertService {

    private var fluxSink: Many<InvitationAlertDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<InvitationAlertDTO> =
        fluxSink
            .asFlux()
            .publish()

    @PostConstruct
    fun init() {
        connectableFlux.connect()
    }

    fun getInvitationFlow(userId: Int): Flux<Boolean> {
        return connectableFlux
            .autoConnect()
            .filter { it.userId == userId }
            .map { it.anyPendingInvitation }
    }

    @Transactional
    fun processEntityChange(alert: InvitationAlertDTO) {
        fluxSink.tryEmitNext(alert).orThrow()
    }
}
