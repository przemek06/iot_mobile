package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.*
import edu.pwr.iotmobile.enums.EComponentType
import edu.pwr.iotmobile.error.exception.InvalidDataException

data class ComponentDTO(
    val id: Int?,
    val name: String,
    val componentType: EComponentType,
    var type: String,
    val size: Int,
    val index: Int,
    val topic: TopicDTO? = null,
    val actionDestinationDTO: ActionDestinationDTO? = null,
    val eventSourceDTO: EventSourceDTO? = null,
    val onSendValue: String? = null,
    val onSendAlternative: String? = null,
    val minValue: String? = null,
    val maxValue: String? = null
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
        inputComponent.topic = topic?.toEntity() ?: throw InvalidDataException()
        inputComponent.onSendValue = onSendValue
        inputComponent.onSendAlternative = onSendAlternative
        inputComponent.minValue = minValue
        inputComponent.maxValue = maxValue

        return inputComponent
    }

    private fun toOutputComponentEntity(): OutputComponent {
        val outputComponent = OutputComponent()
        outputComponent.topic = topic?.toEntity()!!

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
