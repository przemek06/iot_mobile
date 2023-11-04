package edu.pwr.iotmobile.dto

data class TopicMessagesDTO(
    val topicId: Int,
    val messages: List<MessageDTO>
)
