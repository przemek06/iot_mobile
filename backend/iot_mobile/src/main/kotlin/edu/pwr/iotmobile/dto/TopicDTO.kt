package edu.pwr.iotmobile.dto

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

data class TopicDTO(
    var projectId: Int,
    var name: String,
    var valueType:String,
    var isHistoric: Boolean,
    var id: Int
)
