package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ProjectDeletedDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many


@Service
class ProjectDeletedNotificationService {

    private var fluxSink: Many<ProjectDeletedDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<ProjectDeletedDTO> =
        fluxSink
            .asFlux()
            .publish()

    fun getInvitationFlow(userId: Int): Flow<ProjectDeletedDTO> {
        return connectableFlux
            .autoConnect()
            .asFlow()
            .filter { it.userId == userId }
    }

    @Transactional
    fun processEntityChange(alert: ProjectDeletedDTO) {
        fluxSink.tryEmitNext(alert).orThrow()
    }
}
