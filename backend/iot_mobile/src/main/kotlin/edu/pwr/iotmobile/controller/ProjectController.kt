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

    // 400, 401, 403
    @PostMapping("/user/project")
    fun createProject(@Valid @RequestBody projectDTO: ProjectDTO): ResponseEntity<ProjectDTO> {
        return ResponseEntity.ok(projectService.createProject(projectDTO))
    }

    // 400, 401, 403, 404
    @PutMapping("/user/project/key/{projectId}")
    fun regenerateConnectionKey(@PathVariable projectId: Int): ResponseEntity<ProjectDTO> {
        return ResponseEntity.ok(projectService.regenerateConnectionKey(projectId))
    }

    // 401, 403
    @GetMapping("/user/project/active")
    fun findAllProjectsForActiveUser(): ResponseEntity<List<ProjectDTO>> {
        return ResponseEntity.ok(projectService.findAllProjectsForActiveUser())
    }

    // 400, 401, 403
    @GetMapping("/user/project/users/{projectId}")
    fun findAllUsersByProjectId(@PathVariable projectId: Int): ResponseEntity<List<UserInfoDTO>> {
        return ResponseEntity.ok(projectService.findAllUsersByProjectId(projectId))
    }

    // 400, 401, 403, 404
    @GetMapping("/user/project/{projectId}")
    fun findProjectById(@PathVariable projectId: Int): ResponseEntity<ProjectDTO> {
        return ResponseEntity.ok(projectService.findProjectById(projectId))
    }

    // 403, 404
    @PutMapping("/user/project/users/roles")
    fun editProjectRole(@Valid @RequestBody projectRole: ProjectRoleDTO) : ResponseEntity<ProjectRoleDTO> {
        return ResponseEntity.ok(projectService.editRole(projectRole))
    }

    // 400, 403, 404
    @PutMapping("/user/project/users/admin/{projectId}/{userId}")
    fun addProjectAdmin(@PathVariable projectId: Int, @PathVariable userId: Int) : ResponseEntity<ProjectRoleDTO> {
        return ResponseEntity.ok(projectService.addAdmin(userId, projectId))
    }

    // 400, 403
    @GetMapping("/user/project/users/roles/{id}")
    fun findAllProjectRolesByProjectId(@PathVariable id: Int): ResponseEntity<List<ProjectRoleDTO>> {
        return ResponseEntity.ok(projectService.findAllProjectRolesByProjectId(id))
    }

    // 400, 401, 403, 404
    @GetMapping("/user/project/users/roles/active/{projectId}")
    fun findActiveUserProjectRole(@PathVariable projectId: Int): ResponseEntity<ProjectRoleDTO> {
        return ResponseEntity.ok(projectService.findActiveUserProjectRole(projectId))
    }

    // 400, 401, 403
    @DeleteMapping("/user/project/users/{projectId}/{userId}")
    fun revokeAccess(@PathVariable projectId: Int, @PathVariable userId: Int): ResponseEntity<Unit> {
        return if (projectService.revokeAccess(projectId, userId)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
    }

    // 400, 403, 404, 409
    @PostMapping("/user/project/invitation")
    fun createInvitation(@Valid @RequestBody invitationDTO: InvitationDTO): ResponseEntity<InvitationDTO> {
        return ResponseEntity.ok(projectService.createInvitation(invitationDTO))
    }

    // 401, 403
    @GetMapping("/user/project/invitation/active")
    fun findAllInvitationsForActiveUser(): ResponseEntity<List<InvitationDTO>> {
        return ResponseEntity.ok(projectService.findAllInvitationsForActiveUser())
    }

    // 401, 403
    @GetMapping("/user/project/invitation/pending/active")
    fun findAllPendingInvitationsForActiveUser(): ResponseEntity<List<InvitationDTO>> {
        return ResponseEntity.ok(projectService.findAllPendingInvitationsForActiveUser())
    }

    // 400, 401, 403, 404, 409
    @PutMapping("/user/project/invitation/reject/{invitationId}")
    fun rejectInvitation(@PathVariable invitationId: Int): ResponseEntity<InvitationDTO> {
        return ResponseEntity.ok(projectService.rejectInvitation(invitationId))
    }

    // 400, 401, 403, 404, 409
    @PutMapping("/user/project/invitation/accept/{invitationId}")
    fun acceptInvitation(@PathVariable invitationId: Int): ResponseEntity<InvitationDTO> {
        return ResponseEntity.ok(projectService.acceptInvitation(invitationId))
    }
}