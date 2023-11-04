package edu.pwr.iotmobile.dto

data class MailMessageDTO(
    val recipient: String,
    val subject: String,
    val content: String,
    val data: String
)
