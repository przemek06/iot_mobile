package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name="_user")
class User (
    var username: String,
    var password: String,
    var role: String,
    var name: String,
    var isBlocked: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
)