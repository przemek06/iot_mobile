package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.InvitationDTO
import edu.pwr.iotmobile.dto.ProjectDTO
import edu.pwr.iotmobile.dto.ProjectRoleDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.service.ProjectService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProjectController(val projectService: ProjectService) {

    @PostMapping("/user/project")
    fun createProject(@Valid @RequestBody projectDTO: ProjectDTO): ResponseEntity<ProjectDTO> {
        return ResponseEntity.ok(projectService.createProject(projectDTO))
    }

    @PutMapping("/user/project/key/{projectId}")
    fun regenerateConnectionKey(@PathVariable projectId: Int): ResponseEntity<ProjectDTO> {
        return ResponseEntity.ok(projectService.regenerateConnectionKey(projectId))
    }

    @GetMapping("/user/project/active")
    fun findAllProjectsForActiveUser(): ResponseEntity<List<ProjectDTO>> {
        return ResponseEntity.ok(projectService.findAllProjectsForActiveUser())
    }

    @GetMapping("/user/project/users/{id}")
    fun findAllUsersByProjectId(@PathVariable id: Int): ResponseEntity<List<UserInfoDTO>> {
        return ResponseEntity.ok(projectService.findAllUsersByProjectId(id))
    }

    @PutMapping("/user/project/users/roles")
    fun editProjectRole(@Valid @RequestBody projectRole: ProjectRoleDTO) : ResponseEntity<ProjectRoleDTO> {
        return ResponseEntity.ok(projectService.editRole(projectRole))
    }

    @PutMapping("/user/project/users/admin/{projectId}/{userId}")
    fun addProjectAdmin(@PathVariable projectId: Int, @PathVariable userId: Int) : ResponseEntity<ProjectRoleDTO> {
        return ResponseEntity.ok(projectService.addAdmin(userId, projectId))
    }

    @GetMapping("/user/project/users/roles/{id}")
    fun findAllProjectRolesByProjectId(@PathVariable id: Int): ResponseEntity<List<ProjectRoleDTO>> {
        return ResponseEntity.ok(projectService.findAllProjectRolesByProjectId(id))
    }

    @DeleteMapping("/user/project/users/{projectId}/{userId}")
    fun revokeAccess(@PathVariable projectId: Int, @PathVariable userId: Int): ResponseEntity<Unit> {
        return if (projectService.revokeAccess(projectId, userId)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
    }

    @PostMapping("/user/project/invitation")
    fun findAllUsersByProjectId(@Valid @RequestBody invitationDTO: InvitationDTO): ResponseEntity<InvitationDTO> {
        return ResponseEntity.ok(projectService.createInvitation(invitationDTO))
    }

    @GetMapping("/user/project/invitation/active")
    fun findAllInvitationsForActiveUser(): ResponseEntity<List<InvitationDTO>> {
        return ResponseEntity.ok(projectService.findAllInvitationsForActiveUser())
    }

    @GetMapping("/user/project/invitation/pending/active")
    fun findAllPendingInvitationsForActiveUser(): ResponseEntity<List<InvitationDTO>> {
        return ResponseEntity.ok(projectService.findAllPendingInvitationsForActiveUser())
    }

    @PutMapping("/user/project/invitation/reject/{invitationId}")
    fun rejectInvitation(@PathVariable invitationId: Int): ResponseEntity<InvitationDTO> {
        return ResponseEntity.ok(projectService.rejectInvitation(invitationId))
    }

    @PutMapping("/user/project/invitation/accept/{invitationId}")
    fun acceptInvitation(@PathVariable invitationId: Int): ResponseEntity<InvitationDTO> {
        return ResponseEntity.ok(projectService.acceptInvitation(invitationId))
    }
}