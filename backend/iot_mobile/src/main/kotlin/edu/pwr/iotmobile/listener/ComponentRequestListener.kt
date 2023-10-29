package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.service.ComponentChangeService
import edu.pwr.iotmobile.service.ComponentService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
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

    @AfterReturning(
        pointcut = "execution(* edu.pwr.iotmobile.controller.ComponentController.updateAll(..))",
        returning = "response"
    )
    fun propagateChanges(joinPoint: JoinPoint?, response: ResponseEntity<ComponentListDTO>) {
        val dashboardId = response.body?.dashboardId ?: return

        val componentListDto = componentService.findAllByDashboardId(dashboardId)

        componentChangeService.processEntityChange(componentListDto)
    }


}