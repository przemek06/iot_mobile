package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ComponentListDTO
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Service
class ComponentChangeService {

    private var fluxSink: Sinks.Many<ComponentListDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<ComponentListDTO> =
        fluxSink
            .asFlux()
            .publish()

    @PostConstruct
    fun init() {
        connectableFlux.connect()
    }

    fun getEntityChangeFlow(dashboardId: Int): Flux<ComponentListDTO> {
        return connectableFlux
            .autoConnect()
            .filter { it.dashboardId == dashboardId }
    }

    @Transactional
    fun processEntityChange(dto: ComponentListDTO) {
        fluxSink.tryEmitNext(dto).orThrow()
    }
}
