package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.MessageDTO
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Service
class IncomingMessageService {

    private var fluxSink: Sinks.Many<MessageDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<MessageDTO> =
        fluxSink
            .asFlux()
            .publish()

    @PostConstruct
    fun init() {
        connectableFlux.connect()
    }

    fun getEntityChangeFlow(queueList: List<String>): Flux<MessageDTO> {
        return connectableFlux
            .autoConnect()
            .filter{queueList.contains(it.topic.uniqueName)}
    }

    @Transactional
    fun processEntityChange(dto: MessageDTO) {
        fluxSink.tryEmitNext(dto).orThrow()
    }
}
