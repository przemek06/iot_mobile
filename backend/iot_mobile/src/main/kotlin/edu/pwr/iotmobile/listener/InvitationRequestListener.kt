package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.enums.EInvitationStatus
import edu.pwr.iotmobile.service.InvitationChangeService
import edu.pwr.iotmobile.service.ProjectService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component


@Aspect
@Component
class InvitationRequestListener {
    @Autowired
    private lateinit var invitationChangeService: InvitationChangeService

    @Autowired
    lateinit var projectService: ProjectService

    @AfterReturning(
        pointcut = "execution(* edu.pwr.iotmobile.controller.ProjectController.createInvitation(..)) " +
                "|| execution(* edu.pwr.iotmobile.controller.ProjectController.acceptInvitation(..)) " +
                "|| execution(* edu.pwr.iotmobile.controller.ProjectController.rejectInvitation(..))",
        returning = "response"
    )
    fun sendNotification(joinPoint: JoinPoint?, response: ResponseEntity<InvitationDTO>) {
        response.body?.let { afterChange(it) }
    }

    private fun afterChange(entity: InvitationDTO) {
        val userId = entity.userId
        val userInvitations = projectService.findAllInvitationsByUserId(userId)
        val anyPendingInvitation = userInvitations.any { it.status == EInvitationStatus.PENDING }
        invitationChangeService.processEntityChange(InvitationAlertDTO(userId, anyPendingInvitation))
    }

}