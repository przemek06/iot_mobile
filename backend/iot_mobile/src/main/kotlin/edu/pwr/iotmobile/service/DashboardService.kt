package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.DashboardDTO
import edu.pwr.iotmobile.entities.Dashboard
import edu.pwr.iotmobile.error.exception.DashboardAlreadyExistsException
import edu.pwr.iotmobile.error.exception.DashboardNotFoundException
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.repositories.DashboardRepository
import org.springframework.stereotype.Service

@Service
class DashboardService(
    val dashboardRepository: DashboardRepository,
    val projectService: ProjectService,
    val userService: UserService
) {

    fun createDashboard(dashboard: DashboardDTO): DashboardDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        if (!projectService.isEditor(userId, dashboard.projectId))
            throw NotAllowedException()

        if (dashboardRepository.existsByNameAndProjectId(dashboard.name, dashboard.projectId))
            throw DashboardAlreadyExistsException()

        val toSave = dashboard.toEntity()
        return dashboardRepository.save(toSave).toDTO()
    }

    fun deleteDashboard(dashboardId: Int): Boolean {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val dashboard = dashboardRepository.findById(dashboardId)

        if (dashboard.isEmpty)
            return false

        val projectId = dashboard.get().project.id ?: return false

        if (!projectService.isEditor(userId, projectId))
            throw NotAllowedException()

        dashboardRepository.delete(dashboard.get())

        return true
    }

    fun findAllDashboardInProject(projectId: Int): List<DashboardDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()

        if (!projectService.isInProject(userId, projectId))
            throw NotAllowedException()

        return dashboardRepository
            .findAllByProjectId(projectId)
            .map { it.toDTO() }
    }

    fun findById(dashboardId: Int): DashboardDTO {
        return dashboardRepository.findById(dashboardId).orElseThrow { DashboardNotFoundException() }.toDTO()
    }

    fun findEntityById(dashboardId: Int): Dashboard {
        return dashboardRepository.findById(dashboardId).orElseThrow { DashboardNotFoundException() }
    }

}