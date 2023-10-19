package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.DashboardDTO
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.repositories.DashboardRepository
import jakarta.persistence.Id
import org.springframework.stereotype.Service

@Service
class DashboardService(
    val dashboardRepository: DashboardRepository,
    val projectService: ProjectService,
    val userService: UserService
) {

    fun createDashboard(dashboard: DashboardDTO) : DashboardDTO {
        val userId = userService.getActiveUserId()
        if (!projectService.isEditor(userId, dashboard.projectId))
            throw NotAllowedException()

        val toSave = dashboard.toEntity()
        return dashboardRepository.save(toSave).toDTO()
    }

    fun deleteDashboard(dashboardId: Int) : Boolean {
        val userId = userService.getActiveUserId()
        val dashboard = dashboardRepository.findById(dashboardId)

        if (dashboard.isEmpty)
            return false

        val projectId = dashboard.get().project.id ?: return false

        if (!projectService.isEditor(userId, projectId))
            throw NotAllowedException()

        dashboardRepository.delete(dashboard.get())

        return true
    }

    fun findAllDashboardInProject(projectId: Int) : List<DashboardDTO> {
        return dashboardRepository
            .findAllByProjectId(projectId)
            .map { it.toDTO() }
    }

}