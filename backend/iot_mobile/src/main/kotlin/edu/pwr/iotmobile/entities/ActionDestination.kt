package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ActionDestination (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var type: String,
    var token: String
)