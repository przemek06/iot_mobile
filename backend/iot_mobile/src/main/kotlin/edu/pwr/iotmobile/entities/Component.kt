package edu.pwr.iotmobile.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "component_type")
open class Component  (
    @ManyToOne
    @JoinColumn(name = "dashboard_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    open var dashboard: Dashboard,
    open var name: String,
    open var type: String,
    open var size: Int,
    open var index: Int,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Int?=null
) {
    constructor() : this(Dashboard(),"", "", -1, -1)


}