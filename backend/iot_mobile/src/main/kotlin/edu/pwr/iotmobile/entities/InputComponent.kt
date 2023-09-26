package edu.pwr.iotmobile.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

class InputComponent (
    var dashboardId: Int,
    var topicId: Int,
    var type: String,
    var size: Int,
    var xCoordinate: Int,
    var yCoordinate: Int,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
)