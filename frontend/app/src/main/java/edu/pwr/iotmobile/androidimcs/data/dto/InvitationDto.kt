package edu.pwr.iotmobile.androidimcs.data.dto

data class InvitationDto(
    val id: Int,
    val project: ProjectDto,
    val userId: Int,
    val status: String,
)

data class InvitationDtoSend(
    val projectId: Int,
    val userId: Int
)
