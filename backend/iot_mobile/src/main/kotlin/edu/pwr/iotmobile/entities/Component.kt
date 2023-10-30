package edu.pwr.iotmobile.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class Component  (
    @ManyToOne
    @JoinColumn(name = "dashboard_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    open var dashboard: Dashboard,
    open var type: String,
    open var size: Int,
    open var index: Int,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Int?=null
) {
    constructor() : this(Dashboard(), "", -1, -1)


}