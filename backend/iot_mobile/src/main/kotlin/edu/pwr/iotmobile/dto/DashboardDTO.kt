package edu.pwr.iotmobile.dto

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

data class DashboardDTO(
    var projectId: Int,
    var name: String,
    var createdBy: Int,
    var createdAt: LocalDateTime,
    var id: Int
)
