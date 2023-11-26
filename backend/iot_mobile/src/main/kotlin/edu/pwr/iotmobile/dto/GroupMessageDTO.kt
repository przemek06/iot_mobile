package edu.pwr.iotmobile.dto

data class GroupMessageDTO(
    val message: MessageDTO,
    val userIds: List<Int>
)
