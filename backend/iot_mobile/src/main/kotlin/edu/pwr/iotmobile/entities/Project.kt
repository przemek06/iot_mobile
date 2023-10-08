package edu.pwr.iotmobile.entities

import jakarta.persistence.*

@Entity
class Project (
    var name: String,
    var createdBy: Int,
    var connectionKey: String,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "project")
    private var projectRoles = mutableListOf<ProjectRole>()

    constructor() : this("", 0, "")
}