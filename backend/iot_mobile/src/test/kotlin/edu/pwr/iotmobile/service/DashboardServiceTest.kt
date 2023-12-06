package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.Dashboard
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.repositories.DashboardRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


class DashboardServiceTest {

    private val dashboardRepository = mockk<DashboardRepository>()
    private val projectService = mockk<ProjectService>()
    private val userService = mockk<UserService>()
    private val dashboardService = DashboardService(
        dashboardRepository = dashboardRepository,
        projectService = projectService,
        userService = userService
    )

    private fun getNewDashboard(): Dashboard {
        val dashboard = Dashboard()
        dashboard.project.id = 1
        dashboard.name = ""
        return dashboard
    }

    @Test
    fun createDashboardPositive() {
        // given
        val dashboard = getNewDashboard()
        every { userService.getActiveUserId() } returns 1
        every { projectService.isEditor(any(), any()) } returns true
        every { dashboardRepository.existsByNameAndProjectId(any(), any()) } returns false
        every { dashboardRepository.save(any()) } returns dashboard

        //when
        val actual = dashboardService.createDashboard(dashboard.toDTO())

        // then
        assertEquals(dashboard.toDTO(), actual)
    }

    @Test
    fun createDashboardUserNotAuthenticated() {
        // given
        val dashboard = getNewDashboard()
        every { userService.getActiveUserId() } returns null

        // throws
        assertThrows<NoAuthenticationException> { dashboardService.createDashboard(dashboard.toDTO()) }
    }

    @Test
    fun createDashboardUserUnauthorized() {
        // given
        val dashboard = getNewDashboard()
        every { userService.getActiveUserId() } returns 1
        every { projectService.isEditor(any(), any()) } returns false

        // throws
        assertThrows<NotAllowedException> { dashboardService.createDashboard(dashboard.toDTO()) }
    }

    @Test
    fun deleteDashboardPositive() {
        // given
        val dashboard = getNewDashboard()
        every { userService.getActiveUserId() } returns 1
        every { dashboardRepository.findById(any()) } returns Optional.of(dashboard)
        every { projectService.isEditor(any(), any()) } returns true
        every { dashboardRepository.delete(any()) } returns Unit

        // when
        val actual = dashboardService.deleteDashboard(1)

        // then
        assertEquals(true, actual)
    }

    @Test
    fun deleteDashboardDoesNotExist() {
        // given
        every { userService.getActiveUserId() } returns 1
        every { dashboardRepository.findById(any()) } returns Optional.empty()

        // when
        val actual = dashboardService.deleteDashboard(1)
        // then
        assertEquals(false, actual)
    }

    @Test
    fun deleteDashboardUserNotAuthenticated() {
        // given
        every { userService.getActiveUserId() } returns null

        // throws
        assertThrows<NoAuthenticationException> { dashboardService.deleteDashboard(1) }
    }

    @Test
    fun deleteDashboardUserUnauthorized() {
        // given
        val dashboard = getNewDashboard()
        every { userService.getActiveUserId() } returns 1
        every { projectService.isEditor(any(), any()) } returns false
        every { dashboardRepository.findById(any()) } returns Optional.of(dashboard)

        // throws
        assertThrows<NotAllowedException> { dashboardService.deleteDashboard(1) }
    }

}