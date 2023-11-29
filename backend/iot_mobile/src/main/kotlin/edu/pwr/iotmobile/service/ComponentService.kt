package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.entities.Component
import edu.pwr.iotmobile.entities.InputComponent
import edu.pwr.iotmobile.entities.OutputComponent
import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.enums.EComponentType
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
        val existingComponents = componentRepository
            .findAllByDashboardId(componentListDTO.dashboardId)

        val newComponentIds = componentListDTO
            .components
            .mapNotNull { it.id }

        val toDeleteIds = existingComponents
            .filter { it.id !in newComponentIds }
            .mapNotNull { it.id }

        val toPreserve = existingComponents.filter { it.id in newComponentIds }
        toPreserve.forEach {
            it.index = componentListDTO.components
                .find { it2 -> it2.id == it.id }
                ?.index ?: throw InvalidStateException()
        }

        componentRepository.deleteAllById(toDeleteIds)

        val toSave = componentListDTO
            .toEntityList()
            .filter { it.id == null }
            .toMutableList()

        toSave.addAll(toPreserve)
        val saved = componentRepository.saveAll(toSave)

        val savedDTO = entitiesToDTOs(saved)

        return ComponentListDTO(componentListDTO.dashboardId, savedDTO)
    }

    fun findAllTriggerComponents() : List<TriggerComponent> {
        return componentRepository.findAll()
            .filterIsInstance<TriggerComponent>()
    }

    fun findAllByDashboardId(dashboardId: Int): ComponentListDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val projectId = dashboardService.findById(dashboardId).projectId

        if (!projectService.isInProject(userId, projectId)) {
            throw NotAllowedException()
        }

        val entities = componentRepository.findAllByDashboardId(dashboardId)

        val savedDTO = entitiesToDTOs(entities)

        return ComponentListDTO(dashboardId, savedDTO)
    }

    fun findAllEntitiesByDashboardIdNoSecurity(dashboardId: Int) : List<Component> {
        return componentRepository.findAllByDashboardId(dashboardId)
    }

    fun findAllByDashboardIdNoSecurity(dashboardId: Int): ComponentListDTO {
        val entities = componentRepository.findAllByDashboardId(dashboardId)

        val savedDTO = entitiesToDTOs(entities)

        return ComponentListDTO(dashboardId, savedDTO)
    }

    fun entitiesToDTOs(entities: List<Component>): List<ComponentDTO> {
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

    fun findAllByDashboardProjectId(projectId: Int) : List<Component> {
        return componentRepository.findAllByDashboardProjectId(projectId)
    }
    fun findAllByDashboardProjectCreatedById(userId: Int) : List<Component> {
        return componentRepository.findAllByDashboardProjectCreatedById(userId)
    }
}