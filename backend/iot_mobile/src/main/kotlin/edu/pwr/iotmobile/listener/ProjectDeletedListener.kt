package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.dto.ProjectDeletedDTO
import edu.pwr.iotmobile.enums.EInvitationStatus
import edu.pwr.iotmobile.service.InvitationChangeService
import edu.pwr.iotmobile.service.ProjectDeletedNotificationService
import edu.pwr.iotmobile.service.ProjectService
import edu.pwr.iotmobile.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Aspect
@Component
class ProjectDeletedListener {
    @Autowired
    private lateinit var projectDeletedNotificationService: ProjectDeletedNotificationService

    @Autowired
    private lateinit var invitationChangeService: InvitationChangeService

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var userService: UserService


    @Around(
        "execution(* edu.pwr.iotmobile.controller.ProjectController.deleteProjectById(..)) && args(projectId)",
        argNames = "projectId"
    )
    fun sendNotificationsOnProjectAction(pjp: ProceedingJoinPoint, projectId: Int) {
        val projectRoles = projectService.findAllProjectRolesByProjectId(projectId)
        val invitations = projectService.findAllPendingInvitationsByProjectId(projectId)

        val result = pjp.proceed()
        if (result !is ResponseEntity<*>) {
            return
        }

        if (result.statusCode == HttpStatus.OK) {
            projectRoles.map { ProjectDeletedDTO(projectId, it.user.id) }
                .forEach { projectDeletedNotificationService.processEntityChange(it) }
            invitations.forEach { afterChange(it) }
        }
    }

    @Around(
        "execution(* edu.pwr.iotmobile.controller.UserController.deleteActiveUser(..))"
    )
    fun sendNotificationsOnUserAction(pjp: ProceedingJoinPoint) {
        val id = userService.getActiveUserId()
        val projectIds = id?.let {
            projectService.findAllProjectsByCreatedById(it).mapNotNull { it2 -> it2.id  }
        }
        val projectRoles = projectIds?.flatMap { projectService.findAllProjectRolesByProjectId(it) }
        val invitations = projectIds?.flatMap { projectService.findAllPendingInvitationsByProjectId(it) }

        val result = pjp.proceed()
        if (result !is ResponseEntity<*>) {
            return
        }

        if (result.statusCode == HttpStatus.OK) {
            projectRoles?.map { ProjectDeletedDTO(it.projectId, it.user.id) }
                ?.forEach { projectDeletedNotificationService.processEntityChange(it) }

            invitations?.forEach { afterChange(it) }
        }
    }

    private fun afterChange(dto: InvitationDTO) {
        val userId = dto.userId
        val userInvitations = projectService.findAllInvitationsByUserId(userId)
        val anyPendingInvitation = userInvitations.any { it.status == EInvitationStatus.PENDING }
        invitationChangeService.processEntityChange(InvitationAlertDTO(userId, anyPendingInvitation))
    }

}