package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.service.ComponentChangeService
import kotlinx.coroutines.flow.Flow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class ComponentChangeController(
        val componentChangeService: ComponentChangeService
) {
    @MessageMapping("componentChange")
    fun requestResponse(dashboardId: Int): Flow<ComponentListDTO> =
            componentChangeService.getFlowByDashboardId(dashboardId)
}