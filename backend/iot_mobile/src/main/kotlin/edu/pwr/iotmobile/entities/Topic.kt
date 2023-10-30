package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.enums.EValueType
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class Topic (
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var project: Project,
    var name: String,
    @Column(unique = true)
    var uniqueName: String,
    var valueType: EValueType,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this(Project(), "", "", EValueType.TEXT) {

    }
    fun toDTO() : TopicDTO {
        return  TopicDTO(
            id, project.id!!, name, uniqueName, valueType
        )
    }

}