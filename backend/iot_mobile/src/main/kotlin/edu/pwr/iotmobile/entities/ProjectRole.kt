package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ProjectRole (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String,
    var projectId: Int,
    var userId: Int,
    var role: String
)