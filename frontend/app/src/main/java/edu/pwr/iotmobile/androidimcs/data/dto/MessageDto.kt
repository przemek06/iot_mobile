package edu.pwr.iotmobile.androidimcs.data.dto

import java.time.LocalDateTime

data class MessageDto(
    val id: Int?=null,
    val topic: TopicDto,
    val message: String,
    val connectionKey: String,
    val tsSent: LocalDateTime = LocalDateTime.now()
)