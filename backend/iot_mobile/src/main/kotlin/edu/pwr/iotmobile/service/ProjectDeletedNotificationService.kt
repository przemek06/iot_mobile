package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ProjectDeletedDTO
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
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

    @PostConstruct
    fun init() {
        connectableFlux.connect()
    }

    fun getProjectDeletedFlow(userId: Int, projectId: Int): Flux<ProjectDeletedDTO> {
        return connectableFlux
            .autoConnect()
            .filter { it.userId == userId && it.projectId == projectId }
    }

    @Transactional
    fun processEntityChange(alert: ProjectDeletedDTO) {
        fluxSink.tryEmitNext(alert).orThrow()
    }
}
