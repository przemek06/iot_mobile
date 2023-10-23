package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(components = generateComponents())
        }
    }

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

    fun onComponentClick(id: Int) {
        // TODO: implement
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

    private fun generateComponents() = listOf(
        ComponentData(
            id = 1,
            text = "a",
            height = 80.dp
        ),
        ComponentData(
            id = 2,
            text = "b",
            height = 80.dp
        ),
        ComponentData(
            id = 3,
            text = "c",
            height = 300.dp,
            isFullLine = true
        ),
        ComponentData(
            id = 4,
            text = "d",
            height = 80.dp
        ),
        ComponentData(
            id = 5,
            text = "e",
            height = 80.dp
        ),
        ComponentData(
            id = 6,
            text = "f",
            height = 300.dp,
            isFullLine = true
        ),
        ComponentData(
            id = 7,
            text = "g",
            height = 80.dp
        ),
        ComponentData(
            id = 8,
            text = "h",
            height = 80.dp
        ),
        ComponentData(
            id = 9,
            text = "i",
            height = 300.dp,
            isFullLine = true
        ),
        ComponentData(
            id = 10,
            text = "j",
            height = 80.dp
        ),
        ComponentData(
            id = 11,
            text = "k",
            height = 80.dp
        ),
    )
}