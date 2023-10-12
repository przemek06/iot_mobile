package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.ProjectDTO
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

    @GetMapping("/user/project/active")
    fun findAllProjectsForActiveUser(): ResponseEntity<List<ProjectDTO>> {
        return ResponseEntity.ok(projectService.findAllProjectsForActiveUser())
    }

    @PostMapping("/user/project/users/{id}")
    fun findAllUsersByProjectId(@PathVariable id: Int): ResponseEntity<List<UserInfoDTO>> {
        return ResponseEntity.ok(projectService.findAllUsersByProjectId(id))
    }
}