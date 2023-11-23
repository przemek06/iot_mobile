package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.InvitationAlertDTO
import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.dto.ProjectDeletedDTO
import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.enums.EInvitationStatus
import edu.pwr.iotmobile.integration.IntegrationManager
import edu.pwr.iotmobile.service.*
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
    private lateinit var invitationAlertService: InvitationAlertService

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    private lateinit var componentService: ComponentService

    @Autowired
    private lateinit var integrationManager: IntegrationManager

    @Around(
        "execution(* edu.pwr.iotmobile.controller.ProjectController.deleteProjectById(..)) && args(projectId)",
        argNames = "projectId"
    )
    fun sendNotificationsOnProjectAction(pjp: ProceedingJoinPoint, projectId: Int) {
        val projectRoles = projectService.findAllProjectRolesByProjectId(projectId)
        val invitations = projectService.findAllPendingInvitationsByProjectId(projectId)
        val triggerComponents = componentService.findAllByDashboardProjectId(projectId)
            .filterIsInstance<TriggerComponent>()


        val result = pjp.proceed()
        if (result !is ResponseEntity<*>) {
            return
        }

        if (result.statusCode == HttpStatus.OK) {
            triggerComponents.forEach { it.id?.let { it1 -> integrationManager.removeIntegrationAction(it1) } }
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
            projectService.findAllProjectsByCreatedById(it).mapNotNull { it2 -> it2.id }
        }
        val projectRoles = projectIds?.flatMap { projectService.findAllProjectRolesByProjectId(it) }
        val invitations = projectIds?.flatMap { projectService.findAllPendingInvitationsByProjectId(it) }

        val triggerComponents = id?.let {
            componentService.findAllByDashboardProjectCreatedById(it)
                .filterIsInstance<TriggerComponent>()
        }

        val result = pjp.proceed()
        if (result !is ResponseEntity<*>) {
            return
        }

        if (result.statusCode == HttpStatus.OK) {
            triggerComponents?.forEach { it.id?.let { it1 -> integrationManager.removeIntegrationAction(it1) } }

            projectRoles?.map { ProjectDeletedDTO(it.projectId, it.user.id) }
                ?.forEach { projectDeletedNotificationService.processEntityChange(it) }

            invitations?.forEach { afterChange(it) }
        }
    }

    private fun afterChange(dto: InvitationDTO) {
        val userId = dto.userId
        val userInvitations = projectService.findAllInvitationsByUserId(userId)
        val anyPendingInvitation = userInvitations.any { it.status == EInvitationStatus.PENDING }
        invitationAlertService.processEntityChange(InvitationAlertDTO(userId, anyPendingInvitation))
    }

}