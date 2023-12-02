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
import edu.pwr.iotmobile.androidimcs.data.dto.MessageDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto.Companion.toUserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.TopicMessagesDto
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.listener.ComponentChangeWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.listener.MessageReceivedWebSocketListener
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
import java.time.LocalDateTime

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
    private var _userProjectRole: UserProjectRole? = null
    private var _componentListDto: ComponentListDto? = null
    private var _lastMessages: List<TopicMessagesDto> = emptyList()

    private var componentsListener: ComponentChangeWebSocketListener? = null
    private var messageReceivedListener: MessageReceivedWebSocketListener? = null

    override fun onCleared() {
        componentsListener?.closeWebSocket()
        messageReceivedListener?.closeWebSocket()
    }

    fun init(dashboardId: Int, projectId: Int?, dashboardName: String) {
        if (dashboardId == _dashboardId) return

        // Set private variables
        _projectId = projectId
        _dashboardId = dashboardId

        // Save dashboard to last accessed
        projectId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val entity = DashboardEntity(
                    projectId = projectId,
                    dashboardId = dashboardId,
                    dashboardName = dashboardName,
                )
                try {
                    dashboardRepository.saveLastAccessedDashboard(entity)
                } catch (e: Exception) {
                    Log.e("LastAccessed", "Could not save dashboard $dashboardId to last accessed.", e)
                }
            }
        }

        // Begin listening on component changes
        componentsListener?.closeWebSocket()
        componentsListener = ComponentChangeWebSocketListener(
            client = client,
            dashboardId = dashboardId,
            onComponentChangeMessage = { s -> onComponentChangeMessage(s) }
        )

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Get user project role
            projectId ?: run {
                _uiState.update { it.copy(isError = true, isLoading = false) }
                return@launch
            }
            val projectUserInfo = projectRepository
                .getUserProjectRole(projectId)
                ?: run {
                    _uiState.update { it.copy(isError = true, isLoading = false) }
                    return@launch
                }

            val projectRole = projectUserInfo.toUserProjectRole() ?: run {
                _uiState.update { it.copy(isError = true, isLoading = false) }
                return@launch
            }
            _userProjectRole = projectRole

            // Get components
            val components = try {
                componentRepository.getComponentList(dashboardId).sortedBy { it.index }
            } catch (e: Exception) {
                Log.e("Dashboard", "Error getting component", e)
                _uiState.update {
                    it.copy(
                        isError = true,
                        isLoading = false
                    )
                }
                return@launch
            }
            _componentListDto = ComponentListDto(
                dashboardId = dashboardId,
                components = components
            )

            // Start listening on topics for new messages
            val topics = components.mapNotNull { it.topic?.uniqueName }
            messageReceivedListener?.closeWebSocket()

            if (topics.isNotEmpty()) {
                messageReceivedListener = MessageReceivedWebSocketListener(
                    client = client,
                    topicNames = topics,
                    onMessageReceived = { m -> onMessageReceived(m) }
                )
            }

            // Get last messages for all topics for components
            val lastMessages = getLastMessages()
            _lastMessages = lastMessages

            _uiState.update { ui ->
                ui.copy(
                    components = components.mapNotNull { item ->
                        item.topic?.id?.let {
                            val lastMessage = takeLastMessage(it)
                            item.toComponentData(lastMessage, takeGraphData(it))
                        } ?: item.toComponentData(null, emptyList())
                    },
                    menuOptionsList = generateMenuOptions(_userProjectRole),
                    userProjectRole = _userProjectRole,
                    isError = false,
                    isLoading = false
                )
            }
        }
    }


    //// WebSocket //////

    private fun onComponentChangeMessage(data: ComponentListDto) {
        Log.d("Web", "onComponentChangeMessage called")
        Log.d("Web", data.toString())
        _componentListDto = _componentListDto?.copy(
            components = data.components.sortedBy { it.index }
        )
        _uiState.update { ui ->
            ui.copy(
                components = data.components
                    .sortedBy { it.index }
                    .mapNotNull { item ->
                        item.topic?.id?.let {
                            val lastMessage = takeLastMessage(it)
                            item.toComponentData(lastMessage, takeGraphData(it))
                        } ?: item.toComponentData()
                    }
            )
        }
    }

    private fun onMessageReceived(data: MessageDto) {
        Log.d("Web", "onMessageReceived called")
        Log.d("Web", data.toString())
        val topicId = data.topic.id ?: return
        val currentMessages = _lastMessages
        val newMessages = if (topicId !in currentMessages.map { it.topicId }) {
            currentMessages + listOf(
                TopicMessagesDto(
                    topicId = topicId,
                    messages = listOf(data)
                )
            )
        } else {
            currentMessages
                .map {
                    if (it.topicId == topicId)
                        it.copy(
                            messages = it.messages + listOf(data)
                        )
                    else
                        it
                }
        }
        _lastMessages = newMessages

        _uiState.update { ui ->
            ui.copy(components = ui.components.map { item ->
                item.topic?.id?.let {
                    val lastMessage = takeLastMessage(it)
                    item.copy(
                        currentValue = lastMessage,
                        graphData = takeGraphData(it)
                    )
                } ?: item.copy(
                    currentValue = null,
                    graphData = emptyList()
                )
            })
        }
    }

    /////////////////////

    fun getComponentListDto(): ComponentListDto? = _componentListDto

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
        val message = when (item.type) {

            ComponentDetailedType.Button -> { item.onSendValue }

            ComponentDetailedType.Toggle -> {
                val lastValue = value as String
                val isChecked = lastValue == item.onSendValue
                val newValue = if (isChecked) item.onSendAlternativeValue else item.onSendValue
                val newItems = uiState.value.components.map {
                    if (it.id == item.id) {
                        item.copy(currentValue = newValue)
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
            if (message == null) {
                toast.toast("Error while sending message.")
                return@launch
            }

            val connectionKey = getConnectionKey()
            val messageDto = item.toMessageDto(
                value = message.toString(),
                connectionKey = connectionKey,
            )
            if (messageDto == null) {
                toast.toast("Could not send message.")
                return@launch
            }

            Log.d("Message", "messageDto")
            Log.d("Message", messageDto.toString())

            kotlin.runCatching {
                messageRepository.sendMessage(messageDto)
            }.onSuccess {
                if (!it.isSuccess) {
                    toast.toast("Could not send message.")
                }
            }.onFailure {
                toast.toast("Error while sending message.")
            }
        }
    }

    fun onLocalComponentValueChange(item: ComponentData, value: Any?) {
        when (item.type) {

            ComponentDetailedType.Slider -> {
                val newItems = uiState.value.components.map {
                    if (it.id == item.id) {
                        item.copy(currentValue = value?.toString())
                    } else it
                }
                _uiState.update {
                    it.copy(components = newItems)
                }
            }

            else -> { /*Do nothing*/ }

        }
    }

    fun toggleDeleteDashboardDialog() {
        _uiState.update {
            it.copy(isDeleteDashboardDialogVisible = !it.isDeleteDashboardDialogVisible)
        }
    }

    fun onPlaceDraggedComponent(
        visibleItems: List<LazyStaggeredGridItemInfo>,
        windowWidth: Float
    ) {
        viewModelScope.launch {
            val locComponentListDto = _componentListDto
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
            val newList = newOrderedList.mapIndexed { index, data ->
                data.copy(index = index)
            }

            _uiState.update {
                it.copy(
                    draggedComponentId = null,
                    components = newList
                )
            }

            val dto = locComponentListDto?.copy(
                components = newList.map { it.toDto() }.toList()
            )
            dto?.let {
                kotlin.runCatching {
                    componentRepository.updateComponentList(dto)
                }
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

        // Needed to account for "Add component" button
        val toSubtract = if (_userProjectRole == UserProjectRole.VIEWER) 0 else 1

        for (it in visibleItems.subList(toSubtract, visibleItems.size - 1)) {
            val currentItemIndex = it.index-toSubtract
            // Do not consider the original position of the currently dragged item.
            if (currentItemIndex == itemIndex) continue

            // Calculate the current distance squared.
            val currComponent = components.getOrNull(currentItemIndex) ?: return null
            val width = if (draggedComponent.size == 2) windowWidth else windowWidth/2
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

    private suspend fun getLastMessages(): List<TopicMessagesDto> {
        val id = _dashboardId ?: return emptyList()
        return try {
            messageRepository.getLastMessagesForDashboard(id)
        } catch (e: Exception) {
            Log.e("Messages", "Last messages not received", e)
            emptyList()
        }
    }

    private fun takeLastMessage(topicId: Int) = _lastMessages
        .firstOrNull { it.topicId == topicId }
        ?.messages?.lastOrNull()
        ?.message

    private fun takeGraphData(topicId: Int) = _lastMessages
        .firstOrNull { it.topicId == topicId }
        ?.messages?.takeLast(10)
        ?.mapNotNull { it.toGraphData() }
        ?: emptyList()

    private fun MessageDto.toGraphData(): Pair<LocalDateTime, Float>? {
        return try {
            val dateTime = LocalDateTime.parse(tsSent)
            dateTime to message.toFloat()
        } catch(e: Exception) {
            Log.e("GraphData", "Error while converting to graph data", e)
            null
        }
    }

    private suspend fun getConnectionKey(): String? {
        _projectId?.let {
            try {
                val project = projectRepository.getProjectById(it)
                return project?.connectionKey
            } catch (e: Exception) {
                Log.e("Dashboard", "Error getting conneciton key", e)
                return null
            }
        }
        return null
    }

    fun deleteDashboard() {
        _uiState.update { it.copy(isDialogLoading = true) }
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
            _uiState.update { it.copy(isDialogLoading = false) }
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
                onClick = { toggleDeleteDashboardDialog() }
            )
        )
        else -> emptyList()
    }

    companion object {
        const val DASHBOARD_DELETED_EVENT = "dashboardDeleted"
    }
}