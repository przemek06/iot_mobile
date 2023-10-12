package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import android.util.Log
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
            val newList = it.components.mapIndexed { i, item ->
                if (i == index)
                    item.copy(absolutePosition = offset)
                else item
            }
            it.copy(components = newList)
        }
    }

    fun setDraggedComponentIndex(index: Int?) {
        _uiState.update {
            it.copy(draggedComponentIndex = index)
        }
    }

    fun onComponentClick(index: Int) {
        // TODO: implement
    }

    fun onPlaceDraggedComponent(visibleItems: List<LazyStaggeredGridItemInfo>) {
        viewModelScope.launch {
            val currentUiState = uiState.value
            val draggedComponentIndex = currentUiState.draggedComponentIndex ?: return@launch

            val closestIndex = getClosestItemIndex(
                visibleItems = visibleItems,
                components = currentUiState.components,
                draggedComponentIndex = draggedComponentIndex
            ) ?: return@launch

            val newOrderedList = currentUiState.components.toMutableList()
            newOrderedList.removeAt(draggedComponentIndex)
            newOrderedList.add(closestIndex, currentUiState.components[draggedComponentIndex])

            _uiState.update {
                it.copy(
                    draggedComponentIndex = null,
                    components = newOrderedList
                )
            }
        }
    }

    private fun getClosestItemIndex(
        visibleItems: List<LazyStaggeredGridItemInfo>,
        components: List<ComponentData>,
        draggedComponentIndex: Int
    ): Int? {
        var closestIndex = 0
        var diff: Float = Float.MAX_VALUE

        Log.d("pos", "visibleItems: $visibleItems")

        val draggedComponent = components.getOrNull(draggedComponentIndex) ?: return null
        Log.d("pos", "draggedComponent: ${draggedComponent.absolutePosition}")
        for (it in visibleItems) {
            if (it.index == draggedComponentIndex) continue

            Log.d("pos", "it.index: ${it.index}")
            Log.d("pos", "components: $components")
            val currComponent = components.getOrNull(it.index) ?: return null
            Log.d("pos", "currComponent: ${currComponent.absolutePosition}")

            val currDiff = (draggedComponent.absolutePosition - currComponent.absolutePosition).getDistanceSquared()
            Log.d("pos", "currDiff: $currDiff")
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
    )
}