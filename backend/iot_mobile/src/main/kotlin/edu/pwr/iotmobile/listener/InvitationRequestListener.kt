package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.enums.EInvitationStatus
import edu.pwr.iotmobile.service.InvitationChangeService
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
    fun sendNotificationOnInvitationAction(joinPoint: JoinPoint?, response: ResponseEntity<InvitationDTO>) {
        response.body?.let { afterChange(it) }
    }

    @Around(
        "execution(* edu.pwr.iotmobile.controller.ProjectController.deleteProjectById(..)) && args(projectId)",
        argNames = "projectId"
    )
    fun sendNotificationsOnProjectAction(pjp: ProceedingJoinPoint, projectId: Int) {
        val invitations = projectService
            .findAllInvitationsByProjectId(projectId)

        val result = pjp.proceed()
        if (result !is ResponseEntity<*>) {
            return
        }

        if (result.statusCode == HttpStatus.OK) {
            invitations.forEach { afterChange(it) }
        }
    }

    @Around(
        "execution(* edu.pwr.iotmobile.service.UserService.deleteUserById(..)) && args(id)",
        argNames = "id"
    )
    fun sendNotificationsOnUserAction(pjp: ProceedingJoinPoint, id: Int) {
        val invitations = projectService.findAllProjectsByCreatedById(id)
            .map { it.id ?: return }
            .flatMap { projectService.findAllInvitationsByProjectId(it) }

        pjp.proceed()

        invitations.forEach { afterChange(it) }
    }

    private fun afterChange(dto: InvitationDTO) {
        val userId = dto.userId
        val userInvitations = projectService.findAllInvitationsByUserId(userId)
        val anyPendingInvitation = userInvitations.any { it.status == EInvitationStatus.PENDING }
        invitationChangeService.processEntityChange(InvitationAlertDTO(userId, anyPendingInvitation))
    }

}