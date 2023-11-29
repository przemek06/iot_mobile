package edu.pwr.iotmobile.dto

data class GroupNotificationDTO(
    val message: NotificationDTO,
    val userIds: List<Int>
)
