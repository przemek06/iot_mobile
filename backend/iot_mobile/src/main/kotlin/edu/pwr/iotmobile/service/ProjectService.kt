package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.dto.ProjectDTO
import edu.pwr.iotmobile.dto.ProjectRoleDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.entities.Invitation
import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.ProjectRole
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.enums.EInvitationStatus
import edu.pwr.iotmobile.enums.EProjectRole
import edu.pwr.iotmobile.error.exception.*
import edu.pwr.iotmobile.repositories.InvitationRepository
import edu.pwr.iotmobile.repositories.ProjectRepository
import edu.pwr.iotmobile.repositories.ProjectRoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class ProjectService(
    val projectRepository: ProjectRepository,
    val projectRoleRepository: ProjectRoleRepository,
    val invitationRepository: InvitationRepository,
    val userService: UserService
) {

    fun findProjectById(id: Int): ProjectDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()

        if (!projectRoleRepository.existsByUserIdAndProjectId(userId, id)) {
            throw NotAllowedException()
        }

        return projectRepository
            .findById(id)
            .orElseThrow { ProjectNotFoundException() }
            .toDTO()
    }

    private fun findAllProjectsByUserId(userId: Int): List<ProjectDTO> {
        return projectRoleRepository
            .findAllByUserId(userId)
            .map { it.project.toDTO() }
    }

    fun findAllProjectsByCreatedById(userId: Int): List<ProjectDTO> {
        return projectRepository
            .findAllByCreatedById(userId)
            .map { it.toDTO() }
    }

    fun findAllProjectsForActiveUser(): List<ProjectDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        return findAllProjectsByUserId(userId)
    }

    private fun generateConnectionKey(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun saveCreatorProjectRole(project: Project): ProjectRole {
        val projectRole = ProjectRole(project, project.createdBy, EProjectRole.ADMIN)
        return projectRoleRepository.save(projectRole)
    }

    @Transactional
    fun createProject(projectDTO: ProjectDTO): ProjectDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()

        if (projectDTO.createdBy != userId) {
            throw NotAllowedException()
        }

        val project = projectDTO.toEntity()
        project.connectionKey = generateConnectionKey()
        val saved = projectRepository.save(project)
        saveCreatorProjectRole(saved)

        return saved.toDTO()
    }

    @Transactional
    fun deleteProject(projectId: Int) {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        if (!isProjectOwner(userId, projectId))
            throw NotAllowedException()

        projectRepository.deleteById(projectId)
    }

    private fun isProjectOwner(userId: Int, projectId: Int): Boolean {
        val project = projectRepository.findById(projectId).orElseThrow { ProjectNotFoundException() }
        return project.createdBy.id == userId
    }

    fun regenerateConnectionKey(projectId: Int): ProjectDTO {
        if (!isActiveUserProjectAdmin(projectId))
            throw NotAllowedException()

        val project = projectRepository.findById(projectId).orElseThrow { ProjectNotFoundException() }
        project.connectionKey = generateConnectionKey()

        return projectRepository.save(project).toDTO()
    }

    fun findAllUsersByProjectIdNoSecurity(projectId: Int): List<UserInfoDTO> {
        return projectRoleRepository
            .findAllByProjectId(projectId)
            .map { it.user.toUserInfoDTO() }
    }

    fun findAllUsersByProjectId(projectId: Int): List<UserInfoDTO> {
        val projectUserList = projectRoleRepository.findAllByProjectId(projectId)
        val userIdList = projectUserList.map { it.user.id }
        val activeUserId = userService.getActiveUserId() ?: throw NoAuthenticationException()

        if (!userIdList.contains(activeUserId)) {
            throw NotAllowedException()
        }

        return projectUserList.map { it.user.toUserInfoDTO() }
    }

    fun findAllProjectRolesByProjectId(projectId: Int): List<ProjectRoleDTO> {
        val projectUserList = projectRoleRepository.findAllByProjectId(projectId)
        return projectUserList.map { it.toDTO() }
    }

    fun findActiveUserProjectRole(projectId: Int): ProjectRoleDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val projectRole =
            projectRoleRepository.findByUserIdAndProjectId(userId, projectId) ?: throw UserNotFoundException()
        return projectRole.toDTO()
    }

    fun revokeAccess(projectId: Int, userId: Int): Boolean {
        val activeUserId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        if ((!isActiveUserProjectAdmin(projectId)) and (userId != activeUserId))
            throw NotAllowedException()

        val projectRole = projectRoleRepository.findByUserIdAndProjectId(userId, projectId)
            ?: return false

        projectRoleRepository.delete(projectRole)
        return true
    }

    fun editRole(projectRole: ProjectRoleDTO): ProjectRoleDTO {
        if (projectRole.role == null)
            throw InvalidDataException()

        val existing = projectRoleRepository.findByUserIdAndProjectId(projectRole.user.id, projectRole.projectId)
            ?: throw UserNotFoundException()

        if (!isActiveUserProjectAdmin(projectRole.projectId) || existing.project.createdBy.id == projectRole.user.id)
            throw NotAllowedException()

        existing.role = projectRole.role
        return projectRoleRepository.save(existing).toDTO()
    }

    fun addAdmin(userId: Int, projectId: Int): ProjectRoleDTO {
        if (!isActiveUserProjectAdmin(projectId))
            throw NotAllowedException()

        val existing = projectRoleRepository.findByUserIdAndProjectId(userId, projectId)
            ?: throw UserNotFoundException()

        existing.role = EProjectRole.ADMIN
        return projectRoleRepository.save(existing).toDTO()
    }

    private fun pendingInvitationExists(userId: Int, projectId: Int): Boolean {
        return invitationRepository.existsByUserIdAndProjectIdAndStatus(userId, projectId, EInvitationStatus.PENDING)
    }

    private fun userAlreadyInProject(userId: Int, projectId: Int): Boolean {
        return projectRoleRepository.existsByUserIdAndProjectId(userId, projectId)
    }

    fun isActiveUserProjectAdmin(projectId: Int): Boolean {
        val activeUserId = userService.getActiveUserId() ?: return false
        val projectRole = projectRoleRepository.findByUserIdAndProjectId(activeUserId, projectId)
            ?: return false

        return projectRole.role == EProjectRole.ADMIN
    }

    fun createInvitation(invitation: InvitationDTO): InvitationDTO {
        if (!isActiveUserProjectAdmin(invitation.project.id ?: throw InvalidStateException()))
            throw NotAllowedException()

        if (!userService.userExistsById(invitation.userId)) {
            throw UserNotFoundException()
        }

        if (!projectRepository.existsById(invitation.project.id)) {
            throw ProjectNotFoundException()
        }

        if (pendingInvitationExists(invitation.userId, invitation.project.id))
            throw InvitationAlreadyExistsException()

        if (userAlreadyInProject(invitation.userId, invitation.project.id))
            throw UserAlreadyInProjectException()

        val toSave = invitation.toEntity()
        return invitationRepository.save(toSave).toDTO()
    }

    fun findAllInvitationsByUserId(userId: Int): List<InvitationDTO> {
        return invitationRepository.findAllByUserId(userId).map { it.toDTO() }
    }

    fun findAllPendingInvitationsByProjectId(projectId: Int): List<InvitationDTO> {
        return invitationRepository.findAllByProjectIdAndStatus(projectId, EInvitationStatus.PENDING).map { it.toDTO() }
    }

    fun findAllInvitationsForActiveUser(): List<InvitationDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        return findAllInvitationsByUserId(userId)
    }

    fun findAllPendingInvitationsByUserId(userId: Int): List<InvitationDTO> {
        return invitationRepository.findAllByUserIdAndStatus(userId, EInvitationStatus.PENDING).map { it.toDTO() }
    }

    fun findAllPendingInvitationsForActiveUser(): List<InvitationDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        return findAllPendingInvitationsByUserId(userId)
    }

    private fun getInvitationIfAllowed(invitationId: Int): Invitation {
        val invitation = invitationRepository
            .findById(invitationId)
            .orElseThrow { InvitationNotFoundException() }

        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()

        if (invitation.user.id != userId)
            throw NotAllowedException()

        return invitation
    }

    fun rejectInvitation(invitationId: Int): InvitationDTO {
        val invitation = getInvitationIfAllowed(invitationId)

        if (invitation.status != EInvitationStatus.PENDING)
            throw InvitationNotPendingException()

        invitation.status = EInvitationStatus.REJECTED
        return invitationRepository.save(invitation).toDTO()
    }

    private fun addNewUserToProject(user: User, project: Project): ProjectRole {
        val projectRole = ProjectRole(project, user, EProjectRole.VIEWER)
        return projectRoleRepository.save(projectRole)
    }

    @Transactional
    fun acceptInvitation(invitationId: Int): InvitationDTO {
        val invitation = getInvitationIfAllowed(invitationId)

        if (userAlreadyInProject(
                invitation.user.id ?: throw InvalidStateException(),
                invitation.project.id ?: throw InvalidStateException()
            )
        )
            throw UserAlreadyInProjectException()

        if (invitation.status != EInvitationStatus.PENDING)
            throw InvitationNotPendingException()

        invitation.status = EInvitationStatus.ACCEPTED
        addNewUserToProject(invitation.user, invitation.project)
        return invitationRepository.save(invitation).toDTO()
    }

    fun isEditor(userId: Int, projectId: Int): Boolean {
        val projectRole = projectRoleRepository.findByUserIdAndProjectId(userId, projectId)
            ?: return false

        return (projectRole.role in listOf(EProjectRole.ADMIN, EProjectRole.EDITOR))
    }

    fun isInProject(userId: Int, projectId: Int): Boolean {
        projectRoleRepository.findByUserIdAndProjectId(userId, projectId)
            ?: return false

        return true
    }
}