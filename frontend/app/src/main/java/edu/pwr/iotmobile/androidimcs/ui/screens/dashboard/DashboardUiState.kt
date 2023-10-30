package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole

data class DashboardUiState(
    val draggedComponentId: Int? = null,
    val components: List<ComponentData> = emptyList(),
    val menuOptionsList: List<MenuOption> = emptyList(),
    val userProjectRole: UserProjectRole? = null,
)

data class ComponentData(
    val id: Int,
    val text: String,
    val height: Dp,
    val absolutePosition: Offset = Offset.Zero,
    val isFullLine: Boolean = false
)