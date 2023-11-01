package edu.pwr.iotmobile.listener

import edu.pwr.iotmobile.dto.ProjectDeletedDTO
import edu.pwr.iotmobile.service.ProjectDeletedNotificationService
import edu.pwr.iotmobile.service.ProjectService
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
    lateinit var projectService: ProjectService

    @Around(
        "execution(* edu.pwr.iotmobile.controller.ProjectController.deleteProjectById(..)) && args(projectId)",
        argNames = "projectId"
    )
    fun sendNotificationsOnProjectAction(pjp: ProceedingJoinPoint, projectId: Int) {
        val projectRoles = projectService.findAllProjectRolesByProjectId(projectId)

        val result = pjp.proceed()
        if (result !is ResponseEntity<*>) {
            return
        }

        if (result.statusCode == HttpStatus.OK) {
            projectRoles
                .map { ProjectDeletedDTO(projectId, it.user.id) }
                .forEach { projectDeletedNotificationService.processEntityChange(it) }
        }
    }

    @Around(
        "execution(* edu.pwr.iotmobile.service.UserService.deleteUserById(..)) && args(id)",
        argNames = "id"
    )
    fun sendNotificationsOnUserAction(pjp: ProceedingJoinPoint, id: Int) {
        val projectRoles = projectService.findAllProjectsByCreatedById(id)
            .map { it.id ?: return }
            .flatMap { projectService.findAllProjectRolesByProjectId(it) }

        pjp.proceed()

        projectRoles
            .map { ProjectDeletedDTO(it.projectId, it.user.id) }
            .forEach { projectDeletedNotificationService.processEntityChange(it) }
    }

}