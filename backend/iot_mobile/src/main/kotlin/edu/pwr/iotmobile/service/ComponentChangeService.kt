package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Sinks

@Service
class ComponentChangeService(
        val userService: UserService,
        val projectService: ProjectService,
        val dashboardService: DashboardService
) {

    private var fluxSink: Sinks.Many<ComponentListDTO> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    private var connectableFlux: ConnectableFlux<ComponentListDTO> =
        fluxSink
            .asFlux()
            .publish()

    fun getFlowByDashboardId(dashboardId: Int): Flow<ComponentListDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val dashboardDto = dashboardService.findById(dashboardId)
        val isInProject = projectService.isInProject(userId, dashboardDto.projectId)

        if (isInProject)
            return connectableFlux.autoConnect().asFlow()
        throw NotAllowedException()
    }

    @Transactional
    fun processEntityChange(dto: ComponentListDTO) {
        fluxSink.tryEmitNext(dto).orThrow()
    }
}
