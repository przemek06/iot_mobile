package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Message (
    @ManyToOne
    @JoinColumn(name = "topic_id")
    var topic: Topic,
    var message: String,
    var type: String,
    var tsSent: LocalDateTime?=LocalDateTime.now(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
)