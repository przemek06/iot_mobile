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
import edu.pwr.iotmobile.androidimcs.data.dto.MessageDto
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toDto
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.extensions.asEnum
import java.time.LocalDateTime

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

    val currentValue: String?,

    val absolutePosition: Offset = Offset.Zero,
    val isFullLine: Boolean = false,

    val componentType: ComponentType,
    val type: ComponentDetailedType,

    val topic: Topic? = null,

    val onSendValue: String? = null,
    val onSendAlternativeValue: String? = null,
    val maxValue: String? = null,
    val minValue: String? = null,

    val actionDestinationDTO: ActionDestinationDTO? = null,
    val pattern: String? = null
) {
    companion object {
        fun ComponentDto.toComponentData(currentValue: String?): ComponentData? {
            return ComponentData(
                id = id ?: return null,
                name = name ?: "",
                index = index,
                size = size,
                currentValue = currentValue,
                componentType = componentType.asEnum<ComponentType>() ?: return null,
                type = type.asEnum<ComponentDetailedType>() ?: return null,
                topic = topic?.toTopic(),
                onSendValue = onSendValue,
                onSendAlternativeValue = onSendAlternative,
                maxValue = maxValue,
                minValue = minValue,
                actionDestinationDTO = actionDestinationDTO,
                pattern = pattern
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
                onSendAlternative = onSendAlternativeValue.toString(),
                maxValue = maxValue.toString(),
                minValue = minValue.toString(),
                actionDestinationDTO = actionDestinationDTO,
                pattern = pattern
            )
        }

        fun ComponentData.toMessageDto(value: String, connectionKey: String?): MessageDto? {
            return MessageDto(
                topic = topic?.toDto() ?: return null,
                message = value,
                connectionKey = connectionKey ?: return null,
                tsSent = LocalDateTime.now().toString()
            )
        }
    }
}