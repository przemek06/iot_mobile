package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.data.ComponentType
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.ActionDestinationDTO
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentDto
import edu.pwr.iotmobile.androidimcs.data.dto.EventSourceDTO
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toDto
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.extensions.asEnum

data class DashboardUiState(
    val draggedComponentId: Int? = null,
    val components: List<ComponentData> = emptyList(),
    val topics: List<Topic> = emptyList(),
    val menuOptionsList: List<MenuOption> = emptyList(),
    val userProjectRole: UserProjectRole? = null,
)

data class ComponentData(
    val id: Int,
    val name: String,
    val height: Dp = 140.dp,
    val index: Int,
    val size: Int,

    val absolutePosition: Offset = Offset.Zero,
    val isFullLine: Boolean = false,

    val componentType: ComponentType,
    val type: ComponentDetailedType,

    val topic: Topic? = null,

    val onSendValue: Any? = null,
    val onSendAlternativeValue: Any? = null,
    val maxValue: Any? = null,
    val minValue: Any? = null,

    val actionDestinationDTO: ActionDestinationDTO? = null,
    val eventSourceDTO: EventSourceDTO? = null
) {
    companion object {
        fun ComponentDto.toComponentData(): ComponentData? {
            return ComponentData(
                id = id ?: return null,
                name = name ?: "",
                index = index,
                size = size,
                componentType = componentType.asEnum<ComponentType>() ?: return null,
                type = type.asEnum<ComponentDetailedType>() ?: return null,
                topic = topic?.toTopic(),
                onSendValue = onSendValue,
                onSendAlternativeValue = onSendAlternativeValue,
                maxValue = maxValue,
                minValue = minValue,
                actionDestinationDTO = actionDestinationDTO,
                eventSourceDTO = eventSourceDTO
            )
        }

        fun ComponentData.toDto(): ComponentDto {
            return ComponentDto(
                id = id,
                name = name,
                index = index,
                size = size,
                componentType = componentType.name,
                type = type.name,
                topic = topic?.toDto(),
                onSendValue = onSendValue.toString(),
                onSendAlternativeValue = onSendAlternativeValue.toString(),
                maxValue = maxValue.toString(),
                minValue = minValue.toString(),
                actionDestinationDTO = actionDestinationDTO,
                eventSourceDTO = eventSourceDTO,
            )
        }
    }
}