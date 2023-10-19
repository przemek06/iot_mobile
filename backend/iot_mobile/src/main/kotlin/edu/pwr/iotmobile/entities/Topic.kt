package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.TopicDTO
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Topic (
    var project: Project,
    var name: String,
    var valueType:String,
    var isHistoric: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    fun toDTO() : TopicDTO {
        return  TopicDTO(
            id, project.id!!, name, valueType, isHistoric
        )
    }
}