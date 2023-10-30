package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.*
import edu.pwr.iotmobile.enums.EComponentType

data class ComponentDTO(
    val id: Int?,
    val componentType: EComponentType,
    var type: String,
    val size: Int,
    val index: Int,
    val topicId: Int? = null,
    val actionDestinationDTO: ActionDestinationDTO? = null,
    val eventSourceDTO: EventSourceDTO? = null
) {

    fun toEntity(dashboardId: Int): Component {

        val component = if (componentType == EComponentType.INPUT) {
            toInputComponentEntity()
        } else if (componentType == EComponentType.OUTPUT) {
            toOutputComponentEntity()
        } else {
            toTriggerComponentEntity()
        }


        assignCommonFields(component, dashboardId)
        return component
    }

    private fun toInputComponentEntity(): InputComponent {
        val inputComponent = InputComponent()
        val topic = Topic()
        topic.id = topicId
        inputComponent.topic = topic

        return inputComponent
    }

    private fun toOutputComponentEntity(): OutputComponent {
        val outputComponent = OutputComponent()
        val topic = Topic()
        topic.id = topicId
        outputComponent.topic = topic

        return outputComponent
    }

    private fun toTriggerComponentEntity(): TriggerComponent {
        val triggerComponent = TriggerComponent()
        triggerComponent.actionDestination = actionDestinationDTO?.toEntity()!!
        triggerComponent.eventSource = eventSourceDTO?.toEntity()!!

        return triggerComponent
    }

    private fun assignCommonFields(entity: Component, dashboardId: Int): Component {
        entity.id = id
        entity.size = size
        entity.type = type
        entity.index = index
        val dashboard = Dashboard()
        dashboard.id = dashboardId
        entity.dashboard = dashboard

        return entity
    }
}
