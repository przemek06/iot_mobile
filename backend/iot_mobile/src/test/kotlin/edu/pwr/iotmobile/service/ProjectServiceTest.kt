package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.Invitation
import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.ProjectRole
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.enums.EProjectRole
import edu.pwr.iotmobile.error.exception.*
import edu.pwr.iotmobile.repositories.InvitationRepository
import edu.pwr.iotmobile.repositories.ProjectRepository
import edu.pwr.iotmobile.repositories.ProjectRoleRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProjectServiceTest {

    private val projectRepository = mockk<ProjectRepository>()
    private val projectRoleRepository = mockk<ProjectRoleRepository>()
    private val invitationRepository = mockk<InvitationRepository>()
    private val userService = mockk<UserService>()
    private val projectService = ProjectService(
        projectRepository = projectRepository,
        projectRoleRepository = projectRoleRepository,
        invitationRepository = invitationRepository,
        userService = userService
    )

    private fun getUser(): User {
        val user = User()
        user.id = 1
        return user
    }

    private fun getProject(): Project {
        val project = Project()
        project.id = 1
        project.createdBy.id = 3
        return project
    }

    private fun getProjectRole(): ProjectRole {
        val projectRole = ProjectRole()
        projectRole.project = getProject()
        projectRole.role = EProjectRole.ADMIN
        projectRole.user = getUser()
        return projectRole

    }

    private fun getInvitation(): Invitation {
        val invitation = Invitation()
        invitation.project = getProject()
        invitation.user = getUser()
        return invitation
    }

    @Test
    fun revokeAccessPositive() {
        // given
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole
        every { projectRoleRepository.delete(any()) } returns Unit

        // when
        val actual = projectService.revokeAccess(1, 1)

        // then
        assertEquals(actual, true)
    }

    @Test
    fun revokeAccessNoSuchRole() {
        // given
        every { userService.getActiveUserId() } returns 1
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns null

        // when
        val actual = projectService.revokeAccess(1, 1)

        // then
        assertEquals(actual, false)
    }

    @Test
    fun revokeAccessUserNotAuthenticated() {
        // given
        every { userService.getActiveUserId() } returns null

        // throws
        assertThrows<NoAuthenticationException> { projectService.revokeAccess(1, 1) }
    }

    @Test
    fun editRolePositive() {
        // given
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole
        every { projectRoleRepository.save(any()) } returns projectRole

        // when
        val actual = projectService.editRole(projectRole.toDTO())

        // then
        assertEquals(actual, projectRole.toDTO())
    }

    @Test
    fun editRoleNoRole() {
        // given
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns null

        // throws
        assertThrows<UserNotFoundException> { projectService.editRole(projectRole.toDTO()) }
    }

    @Test
    fun editRoleNotAllowed() {
        // given
        val projectRole = getProjectRole()
        projectRole.role = EProjectRole.EDITOR
        every { userService.getActiveUserId() } returns 1
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole

        // throws
        assertThrows<NotAllowedException> { projectService.editRole(projectRole.toDTO()) }
    }

    @Test
    fun createInvitationPositive() {
        // given
        val invitation = getInvitation()
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { userService.userExistsById(any()) } returns true
        every { projectRepository.existsById(any()) } returns true
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole
        every { invitationRepository.existsByUserIdAndProjectIdAndStatus(any(), any(), any()) } returns false
        every { projectRoleRepository.existsByUserIdAndProjectId(any(), any()) } returns false
        every { invitationRepository.save(any()) } returns invitation

        // when
        val actual = projectService.createInvitation(invitation.toDTO())

        // then
        assertEquals(actual, invitation.toDTO())
    }

    @Test
    fun createInvitationAlreadyInProject() {
        // given
        val invitation = getInvitation()
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { userService.userExistsById(any()) } returns true
        every { projectRepository.existsById(any()) } returns true
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole
        every { invitationRepository.existsByUserIdAndProjectIdAndStatus(any(), any(), any()) } returns false
        every { projectRoleRepository.existsByUserIdAndProjectId(any(), any()) } returns true

        // throws
        assertThrows<UserAlreadyInProjectException> { projectService.createInvitation(invitation.toDTO()) }
    }

    @Test
    fun createInvitationAlreadyExists() {
        // given
        val invitation = getInvitation()
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { userService.userExistsById(any()) } returns true
        every { projectRepository.existsById(any()) } returns true
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole
        every { invitationRepository.existsByUserIdAndProjectIdAndStatus(any(), any(), any()) } returns true

        // throws
        assertThrows<InvitationAlreadyExistsException> { projectService.createInvitation(invitation.toDTO()) }
    }

    @Test
    fun createInvitationProjectNotFound() {
        // given
        val invitation = getInvitation()
        val projectRole = getProjectRole()
        every { userService.getActiveUserId() } returns 1
        every { userService.userExistsById(any()) } returns true
        every { projectRepository.existsById(any()) } returns false
        every { projectRoleRepository.findByUserIdAndProjectId(any(), any()) } returns projectRole

        // throws
        assertThrows<ProjectNotFoundException> { projectService.createInvitation(invitation.toDTO()) }
    }

}