package edu.pwr.iotmobile.androidimcs.data.dto

data class ProjectDto(
    val id: Int? = null,
    val name: String,
    val createdBy: Int,
    val connectionKey: String? = null
)
