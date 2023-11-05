package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto
import edu.pwr.iotmobile.androidimcs.model.listener.ComponentChangeWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData.Companion.toComponentData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class DashboardViewModel(
    private val componentRepository: ComponentRepository,
    private val client: OkHttpClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private var _dashboardId: Int? = null
    private var userProjectRole: UserProjectRole? = UserProjectRole.EDITOR
    private var componentListDto: ComponentListDto? = null

    private var componentsListener: ComponentChangeWebSocketListener? = null

    override fun onCleared() {
        componentsListener?.closeWebSocket()
    }

    fun init(dashboardId: Int) {
        if (dashboardId == _dashboardId) return

        componentsListener = ComponentChangeWebSocketListener(
            client = client,
            dashboardId = dashboardId,
            onComponentChangeMessage = { s -> onComponentChangeMessage(s) }
        )

        viewModelScope.launch {
            val components = componentRepository.getComponentList(dashboardId).sortedBy { it.index }
            componentListDto = ComponentListDto(
                dashboardId = dashboardId,
                components = components
            )

            _uiState.update { ui ->
                ui.copy(
                    components = components.mapNotNull { it.toComponentData() },
                    menuOptionsList = generateMenuOptions(userProjectRole),
                    userProjectRole = userProjectRole
                )
            }
        }
    }


    //// WebSocket //////

    private fun onComponentChangeMessage(text: String) {
        Log.d("Web", "onComponentChangeMessage called")
        Log.d("Web", text)
        return
    }

    /////////////////////

    fun getComponentListDto(): ComponentListDto? = componentListDto

    fun setAbsolutePosition(offset: Offset, index: Int) {
        _uiState.update {
            val newList = it.components.map { item ->
                if (item.id == index)
                    item.copy(absolutePosition = offset)
                else item
            }
            it.copy(components = newList)
        }
    }

    fun setDraggedComponentId(id: Int?) {
        _uiState.update {
            it.copy(draggedComponentId = id)
        }
    }

    fun onComponentClick(item: ComponentData, value: Any?) {
        // TODO: implement - different behaviour based on type,
        when (item.type) {

            ComponentDetailedType.Button -> { /* send component value */ }

            ComponentDetailedType.Toggle -> { /* if value == onSend send onAlternative -> else the other way */ }

            else -> {}

        }
    }

    fun onPlaceDraggedComponent(
        visibleItems: List<LazyStaggeredGridItemInfo>,
        windowWidth: Float
    ) {
        viewModelScope.launch {
            val currentUiState = uiState.value
            val draggedComponentId = currentUiState.draggedComponentId ?: return@launch

            val closestIndex = getClosestItemIndex(
                visibleItems = visibleItems,
                components = currentUiState.components,
                draggedComponentId = draggedComponentId,
                windowWidth = windowWidth
            ) ?: return@launch

            val item = currentUiState.components.firstOrNull { it.id == draggedComponentId } ?: return@launch
            val itemIndex = currentUiState.components.indexOf(item)

            val newOrderedList = currentUiState.components.toMutableList()
            newOrderedList.removeAt(itemIndex)
            newOrderedList.add(closestIndex, item)

            _uiState.update {
                it.copy(
                    draggedComponentId = null,
                    components = newOrderedList
                )
            }
        }
    }

    /**
     * Get the index of item closes to the current position of the dragged item by calculating
     * the smallest distance squared with each visible item in the lazy grid.
     * Inputs:
     * - [visibleItems] -> the visible items on the screen from the LazyGrid.
     * - [components] -> all of the components in the LazyGrid.
     * - [draggedComponentId] -> the id of the currently dragged component.
     * Output: the index of the item closest to the currently dragged component.
     */
    private fun getClosestItemIndex(
        visibleItems: List<LazyStaggeredGridItemInfo>,
        components: List<ComponentData>,
        draggedComponentId: Int,
        windowWidth: Float
    ): Int? {
        var closestIndex = 0
        var diff: Float = Float.MAX_VALUE

        val draggedComponent = components.firstOrNull { it.id == draggedComponentId } ?: return null
        val itemIndex = components.indexOf(draggedComponent)

        for (it in visibleItems) {
            // Do not consider the original position of the currently dragged item.
            if (it.index == itemIndex) continue

            // Calculate the current distance squared.
            val currComponent = components.getOrNull(it.index) ?: return null
            val width = if (draggedComponent.isFullLine) windowWidth else windowWidth/2
            val draggedCenterPos = draggedComponent.absolutePosition + Offset(width/2, draggedComponent.height.value/2)
            val currCenterPos = currComponent.absolutePosition + Offset(width/2, currComponent.height.value/2)
            val currDiff = (draggedCenterPos - currCenterPos).getDistanceSquared()

            // If the new distance is smaller than currently saved, assign it together with
            // the closest index.
            if (currDiff < diff) {
                diff = currDiff
                closestIndex = it.index
            }
        }
        return closestIndex
    }

    private fun generateMenuOptions(role: UserProjectRole?) = when (role) {
        UserProjectRole.ADMIN, UserProjectRole.EDITOR -> listOf(
            MenuOption(
                titleId = R.string.s20,
                onClick = {/*TODO*/}
            ),
            MenuOption(
                titleId = R.string.s21,
                onClick = {/*TODO*/}
            )
        )
        else -> emptyList()
    }
}