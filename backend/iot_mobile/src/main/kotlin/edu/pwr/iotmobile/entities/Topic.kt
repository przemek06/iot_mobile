package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.TopicDTO
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
    var project: Project?,
    var name: String,
    var valueType:String?,
    var isHistoric: Boolean?=false,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
){
    fun toDTO(): TopicDTO{
        return TopicDTO(project?.toDTO(), name, valueType, isHistoric, id)
    }

}