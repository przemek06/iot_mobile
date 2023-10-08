package edu.pwr.iotmobile.entities

import jakarta.persistence.*

@Entity
class ProjectRole (
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var project: Project,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var user: User,
    var role: String,
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?=null

    constructor() : this(Project(), User(), "")
}