package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.enums.EValueType
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["project_id", "name"])]
)
class Topic (
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var project: Project,
    var name: String,
    var valueType: EValueType,
    var isHistoric: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this(Project(), "", EValueType.TEXT, false) {

    }

    fun toDTO() : TopicDTO {
        return  TopicDTO(
            id, project.id!!, name, valueType, isHistoric
        )
    }
}