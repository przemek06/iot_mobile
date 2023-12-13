package edu.pwr.iotmobile.androidimcs.data.dto

data class MessageDto(
    val id: Int?=null,
    val topic: TopicDto,
    val message: String,
    val connectionKey: String,
    val tsSent: String,
)