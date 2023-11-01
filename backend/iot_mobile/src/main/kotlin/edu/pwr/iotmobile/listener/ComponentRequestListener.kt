package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.service.ComponentChangeService
import edu.pwr.iotmobile.service.ComponentService
import edu.pwr.iotmobile.service.DashboardService
import edu.pwr.iotmobile.service.ProjectService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Aspect
@Component
class ComponentRequestListener
{

    @Autowired
    private lateinit var componentChangeService: ComponentChangeService
    @Autowired
    private lateinit var componentService: ComponentService
    @Autowired
    private lateinit var dashboardService: DashboardService
    @Autowired
    lateinit var projectService: ProjectService

    @AfterReturning(
        pointcut = "execution(* edu.pwr.iotmobile.controller.ComponentController.updateAll(..))",
        returning = "response"
    )
    fun propagateChangesOnComponentAction(joinPoint: JoinPoint?, response: ResponseEntity<ComponentListDTO>) {
        val dashboardId = response.body?.dashboardId ?: return

        val componentListDto = componentService.findAllByDashboardIdNoSecurity(dashboardId)

        componentChangeService.processEntityChange(componentListDto)
    }

    @Around(
        "execution(* edu.pwr.iotmobile.controller.DashboardController.deleteDashboard(..)) && args(dashboardId)",
        argNames = "dashboardId"
    )
    fun propagateChangesOnDashboardAction(pjp: ProceedingJoinPoint, dashboardId: Int) {
        val componentListDto = componentService.findAllByDashboardIdNoSecurity(dashboardId)

        val result = pjp.proceed()

        if (result !is ResponseEntity<*>)
            return

        if (result.statusCode == HttpStatus.OK) {
            val toSend = ComponentListDTO(componentListDto.dashboardId, emptyList())
            componentChangeService.processEntityChange(toSend)
        }
    }

}