package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.enums.EComponentType
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.integration.IntegrationManager
import edu.pwr.iotmobile.service.ComponentChangeService
import edu.pwr.iotmobile.service.ComponentService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Aspect
@Component
class ComponentRequestListener {

    @Autowired
    private lateinit var componentChangeService: ComponentChangeService

    @Autowired
    private lateinit var componentService: ComponentService

    @Autowired
    private lateinit var integrationManager: IntegrationManager

    @Around(
        "execution(* edu.pwr.iotmobile.controller.ComponentController.updateAll(..)) && args(componentListDTO)",
        argNames = "componentListDTO"
    )
    fun propagateChangesOnComponentAction(pjp: ProceedingJoinPoint, componentListDTO: ComponentListDTO) : ResponseEntity<*> {
        val dashboardId = componentListDTO.dashboardId
        val oldComponentListDto = componentService.findAllByDashboardIdNoSecurity(dashboardId)

        val result = pjp.proceed()

        if (result !is ResponseEntity<*>) throw InvalidStateException()

        if (result.body !is ComponentListDTO) return result

        val resultComponentListDTO = result.body as ComponentListDTO

        val toDelete =
            oldComponentListDto.components.filter { it.id !in resultComponentListDTO.components.map { it2 -> it2.id } }
                .filter { it.componentType == EComponentType.TRIGGER }

        val toAdd =
            resultComponentListDTO.components.filter { it.id !in oldComponentListDto.components.map { it2 -> it2.id } }
                .filter { it.componentType == EComponentType.TRIGGER }

        toAdd.forEach { integrationManager.addIntegrationAction(it.toEntity(dashboardId) as TriggerComponent) }
        toDelete.forEach { it.id?.let { it1 -> integrationManager.removeIntegrationAction(it1) } }

        componentChangeService.processEntityChange(resultComponentListDTO)
        return result
    }

    @Around(
        "execution(* edu.pwr.iotmobile.controller.DashboardController.deleteDashboard(..)) && args(dashboardId)",
        argNames = "dashboardId"
    )
    fun propagateChangesOnDashboardAction(pjp: ProceedingJoinPoint, dashboardId: Int) {
        val componentListDto = componentService.findAllByDashboardIdNoSecurity(dashboardId)

        val result = pjp.proceed()

        if (result !is ResponseEntity<*>) return

        if (result.statusCode == HttpStatus.OK) {
            componentListDto.components
                .filter { it.componentType == EComponentType.TRIGGER }
                .forEach { it.id?.let { it1 -> integrationManager.removeIntegrationAction(it1) } }

            val toSend = ComponentListDTO(componentListDto.dashboardId, emptyList())
            componentChangeService.processEntityChange(toSend)
        }
    }
}