package edu.pwr.iotmobile.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Topic (
    @ManyToOne
    @JoinColumn(name = "project_id")
    var project: Project?=null,
    var name: String,
    var valueType:String?=null,
    var isHistoric: Boolean?=false,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
)