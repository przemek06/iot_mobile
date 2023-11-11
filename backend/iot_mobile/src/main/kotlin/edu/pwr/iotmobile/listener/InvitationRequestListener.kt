package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.enums.EInvitationStatus
import edu.pwr.iotmobile.service.InvitationAlertService
import edu.pwr.iotmobile.service.ProjectService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component


@Aspect
@Component
class InvitationRequestListener {
    @Autowired
    private lateinit var invitationAlertService: InvitationAlertService

    @Autowired
    lateinit var projectService: ProjectService

    @AfterReturning(
        pointcut = "execution(* edu.pwr.iotmobile.controller.ProjectController.createInvitation(..)) " +
                "|| execution(* edu.pwr.iotmobile.controller.ProjectController.acceptInvitation(..)) " +
                "|| execution(* edu.pwr.iotmobile.controller.ProjectController.rejectInvitation(..))",
        returning = "response"
    )
    fun sendNotificationOnInvitationAction(joinPoint: JoinPoint?, response: ResponseEntity<InvitationDTO>) {
        response.body?.let { afterChange(it) }
    }

    private fun afterChange(dto: InvitationDTO) {
        val userId = dto.userId
        val userInvitations = projectService.findAllInvitationsByUserId(userId)
        val anyPendingInvitation = userInvitations.any { it.status == EInvitationStatus.PENDING }
        invitationAlertService.processEntityChange(InvitationAlertDTO(userId, anyPendingInvitation))
    }

}