package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Message (
    var topicId: Int,
    var message: String,
    var type: String,
    var tsSent: LocalDateTime,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
)