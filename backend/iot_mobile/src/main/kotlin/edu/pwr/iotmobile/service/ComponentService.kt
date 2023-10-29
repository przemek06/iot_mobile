package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.entities.Component
import edu.pwr.iotmobile.entities.InputComponent
import edu.pwr.iotmobile.entities.OutputComponent
import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.repositories.ComponentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ComponentService(
    val componentRepository: ComponentRepository,
    val userService: UserService,
    val projectService: ProjectService,
    val dashboardService: DashboardService
) {

    @Transactional
    fun updateAll(componentListDTO: ComponentListDTO): ComponentListDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val projectId = dashboardService.findById(componentListDTO.dashboardId).projectId

        if (!projectService.isEditor(userId, projectId)) {
            throw NotAllowedException()
        }

        deleteAllInDashboard(componentListDTO.dashboardId)
        val toSave = componentListDTO.toEntityList()
        val saved = componentRepository.saveAll(toSave)

        val savedDTO = entitiesToDTOs(saved)

        return ComponentListDTO(componentListDTO.dashboardId, savedDTO)
    }

    fun deleteAllInDashboard(dashboardId: Int) {
        componentRepository.deleteAllByDashboardId(dashboardId)
    }

    fun findAllByDashboardId(dashboardId: Int): ComponentListDTO {
        val entities = componentRepository.findAllByDashboardId(dashboardId)

        val savedDTO = entitiesToDTOs(entities)

        return ComponentListDTO(dashboardId, savedDTO)
    }

    private fun entitiesToDTOs(entities: List<Component>): List<ComponentDTO> {
        return entities.map {
            if (it is InputComponent) {
                it.toDTO()
            } else if (it is OutputComponent) {
                it.toDTO()
            } else if (it is TriggerComponent) {
                it.toDTO()
            } else throw InvalidStateException()
        }
    }
}