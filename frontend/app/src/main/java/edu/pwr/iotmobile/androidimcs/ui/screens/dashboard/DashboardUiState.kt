package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class DashboardUiState(
    val draggedComponentIndex: Int? = null,
    val components: List<ComponentData> = emptyList()
)

data class ComponentData(
    val id: Int,
    val text: String,
    val height: Dp,
    val absolutePosition: Offset = Offset.Zero,
    val isFullLine: Boolean = false
)