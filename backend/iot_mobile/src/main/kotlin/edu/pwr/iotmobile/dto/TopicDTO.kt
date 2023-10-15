package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Topic

data class TopicDTO(
    var project: ProjectDTO,
    var name: String,
    var valueType:String,
    var isHistoric: Boolean,
    var id: Int
){
    fun toEntity(): Topic{
        return Topic(
            project.toEntity(), name, valueType, isHistoric
        )
    }
}