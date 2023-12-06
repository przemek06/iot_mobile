package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.dto.DashboardDTO
import edu.pwr.iotmobile.entities.Component
import edu.pwr.iotmobile.entities.InputComponent
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.repositories.ComponentRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class ComponentServiceTest {

    private val userService = mockk<UserService>()
    private val projectService = mockk<ProjectService>()
    private val dashboardService = mockk<DashboardService>()
    private val componentRepository = mockk<ComponentRepository>()
    private val componentService = ComponentService(
        userService = userService,
        projectService = projectService,
        dashboardService = dashboardService,
        componentRepository = componentRepository
    )

    private fun getComponent(id: Int): InputComponent {
        val component = InputComponent()
        component.id = id
        component.topic.project.id = 1
        return component
    }

    private fun getSavedComponents(): List<Component> {
        val firstSavedComponent = getComponent(1)
        val secondSavedComponent = getComponent(2)
        return listOf(firstSavedComponent, secondSavedComponent)
    }

    private fun getNewComponents(): ComponentListDTO {
        val firstNewComponent = getComponent(3)
        val secondNewComponent = getComponent(2)
        return ComponentListDTO(1, listOf(firstNewComponent, secondNewComponent).map { it.toDTO() })
    }

    private fun getNewSavedComponents(): List<Component> {
        val firstNewSavedComponent = getComponent(1)
        val secondNewSavedComponent = getComponent(2)
        return listOf(firstNewSavedComponent, secondNewSavedComponent)
    }

    private fun getNewSavedComponentsDto(): ComponentListDTO {
        val firstNewSavedComponent = getComponent(1)
        val secondNewSavedComponent = getComponent(2)
        return ComponentListDTO(1, listOf(firstNewSavedComponent, secondNewSavedComponent).map { it.toDTO() })
    }


    @Test
    fun updateAllPositiveCase() {
        // given
        val newComponents = getNewComponents()
        val savedComponents = getSavedComponents()
        val newSavedComponents = getNewSavedComponents()
        val newSavedDtoComponents = getNewSavedComponentsDto()

        // when
        every { userService.getActiveUserId() } returns 1
        every { dashboardService.findById(any()) } returns DashboardDTO(1, "", 1)
        every { projectService.isEditor(any(), any()) } returns true
        every { componentRepository.findAllByDashboardId(any()) } returns savedComponents
        every { componentRepository.deleteAllById(any()) } returns Unit
        every { componentRepository.saveAll(any() as List<Component>) } returns newSavedComponents

        // then
        val actual = componentService.updateAll(newComponents)
        assertEquals(actual, newSavedDtoComponents)
    }

    @Test
    fun updateAllUserNotAuthenticated() {
        // given
        val newComponents = getNewComponents()
        every { userService.getActiveUserId() } returns null
        // throws
        assertThrows<NoAuthenticationException> { componentService.updateAll(newComponents) }
    }


}