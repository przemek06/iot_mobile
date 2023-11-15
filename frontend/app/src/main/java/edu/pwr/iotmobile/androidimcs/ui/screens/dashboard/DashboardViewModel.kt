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
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.listener.ComponentChangeWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository
import edu.pwr.iotmobile.androidimcs.model.repository.MessageRepository
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData.Companion.toComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData.Companion.toDto
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData.Companion.toMessageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

private const val TAG = "DashboardViewModel"

class DashboardViewModel(
    private val componentRepository: ComponentRepository,
    private val dashboardRepository: DashboardRepository,
    private val messageRepository: MessageRepository,
    private val projectRepository: ProjectRepository,
    private val client: OkHttpClient,
    val toast: Toast,
    val event: Event
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private var _dashboardId: Int? = null
    private var _projectId: Int? = null
    private var userProjectRole: UserProjectRole? = UserProjectRole.EDITOR
    private var componentListDto: ComponentListDto? = null

    private var componentsListener: ComponentChangeWebSocketListener? = null

    override fun onCleared() {
        componentsListener?.closeWebSocket()
    }

    fun init(dashboardId: Int, projectId: Int?) {
        if (dashboardId == _dashboardId) return
        _projectId = projectId

        componentsListener?.closeWebSocket()
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

    private fun onComponentChangeMessage(data: ComponentListDto) {
        Log.d("Web", "onComponentChangeMessage called")
        Log.d("Web", data.toString())
        _uiState.update {
            it.copy(
                components = data.components.mapNotNull { it.toComponentData() }
            )
        }
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
        val message = when (item.type) {

            ComponentDetailedType.Button -> { item.onSendValue }

            ComponentDetailedType.Toggle -> {
                /* if value == onSend send onAlternative -> else the other way */
                val checked = value as Boolean
                val newValue = if (checked) item.onSendAlternativeValue else item.onSendValue
                val newItems = uiState.value.components.map {
                    if (it.id == item.id) {
                        Log.d("click", "item found")
                        item.copy(topic = item.topic?.copy(currentValue = newValue))
                    }
                    else it
                }
                _uiState.update {
                    it.copy(components = newItems)
                }
                newValue
            }

            else -> value

        }

        viewModelScope.launch(Dispatchers.Default) {
            val connectionKey = getConnectionKey()
            val messageDto = item.toMessageDto(
                value = message.toString(),
                connectionKey = connectionKey
            )
            if (messageDto == null) {
                toast.toast("Could not send message.")
                return@launch
            }

            val result = messageRepository.sendMessage(messageDto)
            if (!result.isSuccess){
                toast.toast("Could not send message.")
            }
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
            newOrderedList.mapIndexed { index, data ->
                data.copy(index = index)
            }

            _uiState.update {
                it.copy(
                    draggedComponentId = null,
                    components = newOrderedList
                )
            }

            val dto = componentListDto?.copy(
                components = newOrderedList.map { it.toDto() }.toList()
            )
            dto?.let {
                componentRepository.updateComponentList(dto)
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

        for (it in visibleItems.subList(1, visibleItems.size)) {
            val currentItemIndex = it.index-1
            // Do not consider the original position of the currently dragged item.
            if (currentItemIndex == itemIndex) continue

            // Calculate the current distance squared.
            val currComponent = components.getOrNull(currentItemIndex) ?: return null
            val width = if (draggedComponent.isFullLine) windowWidth else windowWidth/2
            val draggedCenterPos = draggedComponent.absolutePosition + Offset(width/2, draggedComponent.height.value/2)
            val currCenterPos = currComponent.absolutePosition + Offset(width/2, currComponent.height.value/2)
            val currDiff = (draggedCenterPos - currCenterPos).getDistanceSquared()

            // If the new distance is smaller than currently saved, assign it together with
            // the closest index.
            if (currDiff < diff) {
                diff = currDiff
                closestIndex = currentItemIndex
            }
        }
        return closestIndex
    }

    private suspend fun getConnectionKey(): String? {
        _projectId?.let {
            val project = projectRepository.getProjectById(it)
            return project?.connectionKey
        }
        return null
    }

    private fun deleteDashboard() {
        viewModelScope.launch(Dispatchers.Default) {
            val dashboardId = _dashboardId ?: return@launch
            kotlin.runCatching {
                dashboardRepository.deleteDashboard(dashboardId)
            }.onSuccess {
                toast.toast("Successfully deleted dashboard!")
                event.event(DASHBOARD_DELETED_EVENT)
            }.onFailure {
                Log.d(TAG, "Delete dashboard error")
            }
        }
    }

    private fun generateMenuOptions(role: UserProjectRole?) = when (role) {
        UserProjectRole.ADMIN, UserProjectRole.EDITOR -> listOf(
            MenuOption(
                titleId = R.string.s20,
                onClick = {/*TODO*/}
            ),
            MenuOption(
                titleId = R.string.s21,
                onClick = { deleteDashboard() }
            )
        )
        else -> emptyList()
    }

    companion object {
        const val DASHBOARD_DELETED_EVENT = "dashboardDeleted"
    }
}