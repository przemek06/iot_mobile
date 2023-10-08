package edu.pwr.iotmobile.mapper

import edu.pwr.iotmobile.dto.ProjectDTO
import edu.pwr.iotmobile.entities.Project
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProjectMapper {
    fun toDto(project: Project): ProjectDTO
    fun toDto(projects: List<Project>): List<ProjectDTO>
    fun toBean(projectDTO: ProjectDTO): Project
    fun toBean(projectDTOs: List<ProjectDTO>): List<Project>
}