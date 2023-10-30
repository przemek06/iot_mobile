package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many


@Service
class InvitationChangeService {

    private var fluxSink: Many<InvitationAlertDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<InvitationAlertDTO> =
        fluxSink
            .asFlux()
            .publish()

    fun getInvitationFlow(userId: Int): Flow<Boolean> {
        return connectableFlux
            .autoConnect()
            .asFlow()
            .filter { it.userId == userId }
            .map { it.anyPendingInvitation }
    }

    @Transactional
    fun processEntityChange(alert: InvitationAlertDTO) {
        fluxSink.tryEmitNext(alert).orThrow()
    }
}
