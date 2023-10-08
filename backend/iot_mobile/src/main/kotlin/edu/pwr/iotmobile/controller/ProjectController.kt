package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.ProjectDTO
import edu.pwr.iotmobile.mapper.ProjectMapper
import edu.pwr.iotmobile.service.ProjectService
import org.mapstruct.factory.Mappers
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("projects")
class ProjectController(
    val projectService: ProjectService,
    val projectMapper: ProjectMapper
) {


    @GetMapping("/user/{id}")
    fun getByUserId(@PathVariable userId: Int):ResponseEntity<List<ProjectDTO>>{
        val projects = projectService.getByUserId(userId)
        return ResponseEntity.ok(projectMapper.toDto(projects))
    }

}