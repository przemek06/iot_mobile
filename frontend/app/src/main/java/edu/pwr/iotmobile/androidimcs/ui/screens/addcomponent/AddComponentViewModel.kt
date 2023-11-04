package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.data.ComponentType
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentDto
import edu.pwr.iotmobile.androidimcs.data.scopestates.ComponentsListState
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.scope.ScopeID

private val BottomNavDataList = listOf(
    AddComponentViewModel.BottomNavData(),
    AddComponentViewModel.BottomNavData(hasPrevButton = true),
    AddComponentViewModel.BottomNavData(
        nextButtonText = R.string.confirm,
        hasPrevButton = true
    )
)

class AddComponentViewModel(
    private val topicRepository: TopicRepository,
    private val componentRepository: ComponentRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddComponentUiState())
    val uiState = _uiState.asStateFlow()

    private var _projectId: Int? = null

    fun init(projectId: Int) {
        if (_projectId == null || projectId != _projectId) {
            _projectId = projectId
            viewModelScope.launch(Dispatchers.Default) {
                val topics = getTopicsForProject(projectId)
                _uiState.update {
                    it.copy(
                        inputComponents = generateInputComponents(),
                        topics = topics
                    )
                }
            }
        }
    }

    fun navigateNext(scopeID: ScopeID) {
        when (_uiState.value.currentPage) {
            AddComponentPage.ChooseComponent ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.ChooseTopic,
                    bottomNavData = getBottomNavData(AddComponentPage.ChooseTopic)
                ) }
            AddComponentPage.ChooseTopic ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.Settings,
                    bottomNavData = getBottomNavData(AddComponentPage.Settings),
                    settings = generateSettings()
                ) }
            AddComponentPage.Settings ->
                onConfirmComponent(scopeID)
        }
    }

    fun navigatePrevious() {
        when (_uiState.value.currentPage) {
            AddComponentPage.ChooseTopic ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.ChooseComponent,
                    bottomNavData = getBottomNavData(AddComponentPage.ChooseComponent)
                ) }
            AddComponentPage.Settings ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.ChooseTopic,
                    bottomNavData = getBottomNavData(AddComponentPage.ChooseTopic)
                ) }
            else -> { /*Do nothing*/ }
        }
    }

    fun onChooseComponent(componentData: ComponentChoiceData) {
        _uiState.update {
            it.copy(chosenComponentType = componentData.type)
        }
    }

    fun onChooseTopic(topic: Topic) {
        _uiState.update {
            it.copy(chosenTopic= topic)
        }
    }

    fun onTextChange(
        type: SettingType,
        text: String
    ) {
        _uiState.update { ui ->
            val input = ui.settings[type] ?: return
            val inputField = input.copy(inputFieldData = input.inputFieldData.copy(text = text))
            val newSettings = ui.settings.toMutableMap()
            newSettings.replace(type, inputField)
            ui.copy(settings = newSettings)
        }
    }

    private fun onConfirmComponent(scopeID: ScopeID) {
        viewModelScope.launch(Dispatchers.Default) {
            val data = getComponentDtoData() ?: return@launch
            val componentListDto = ComponentsListState.getScoped(scopeID)?.componentListDto ?: return@launch

            val newComponents = (componentListDto.components + listOf(data))
                .mapIndexed { index, item -> item.copy(index = index) }
            val newDto = componentListDto.copy(components = newComponents)

            kotlin.runCatching {
                componentRepository.updateComponentList(newDto)
            }.onSuccess {
                event.event(ADD_COMPONENT_SUCCESS_EVENT)
                toast.toast(SUCCESS_TOAST_MESSAGE)
            }.onFailure {
                toast.toast(FAILURE_TOAST_MESSAGE)
            }
        }
    }

    private fun getComponentDtoData(): ComponentDto? {
        val locUiState = _uiState.value
        // TODO: think of something better
        return ComponentDto(
            componentType = ComponentType.INPUT.name, // TODO
            type = locUiState.chosenComponentType?.name ?: return null,
            size = 1, // TODO
            topicId = locUiState.chosenTopic?.id,
            name = locUiState.settings[SettingType.Name]?.inputFieldData?.text,
            defaultValue = locUiState.settings[SettingType.DefaultValue]?.inputFieldData?.text,
            onSendValue = locUiState.settings[SettingType.OnClickSend]?.inputFieldData?.text ?: locUiState.settings[SettingType.OnToggleOnSend]?.inputFieldData?.text,
            onSendAlternativeValue = locUiState.settings[SettingType.OnToggleOffSend]?.inputFieldData?.text,
            maxValue = locUiState.settings[SettingType.MaxValue]?.inputFieldData?.text,
            minValue = locUiState.settings[SettingType.MinValue]?.inputFieldData?.text
        )
    }

    private suspend fun getTopicsForProject(projectId: Int): List<Topic> {
        kotlin.runCatching {
            topicRepository.getTopicsByProjectId(projectId)
        }.onSuccess { topics ->
            return topics.mapNotNull { it.toTopic() }
        }.onFailure {
            return emptyList()
        }
        return emptyList()
    }

    private fun getBottomNavData(page: AddComponentPage) = when (page) {
        AddComponentPage.ChooseComponent -> BottomNavDataList[0]
        AddComponentPage.ChooseTopic -> BottomNavDataList[1]
        AddComponentPage.Settings -> BottomNavDataList[2]
    }

    private fun generateSettings(): Map<SettingType, SettingData> {
        val defaultFields = mapOf(
            SettingType.Name to SettingData(
                title = R.string.s39,
                inputFieldData = InputFieldData(
                    label = R.string.name
                )
            ),
            SettingType.DefaultValue to SettingData(
                title = R.string.s40,
                inputFieldData = InputFieldData(
                    label = R.string.s34
                )
            )
        )

        val specificFields = when (uiState.value.chosenComponentType) {

            ComponentDetailedType.Button -> mapOf(
                SettingType.OnClickSend to SettingData(
                    title = R.string.s33,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                )
            )

            ComponentDetailedType.Toggle -> mapOf(
                SettingType.OnToggleOnSend to SettingData(
                    title = R.string.s35,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                ),
                SettingType.OnToggleOffSend to SettingData(
                    title = R.string.s36,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                )
            )

            ComponentDetailedType.Slider -> mapOf(
                SettingType.MaxValue to SettingData(
                    title = R.string.s37,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                ),
                SettingType.MinValue to SettingData(
                    title = R.string.s38,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                )
            )

            else -> emptyMap()

        }

        return defaultFields + specificFields
    }

    private fun generateInputComponents() = listOf(
        ComponentChoiceData(
            titleId = R.string.s41,
            iconRes = R.drawable.ic_button,
            type = ComponentDetailedType.Button
        ),
        ComponentChoiceData(
            titleId = R.string.s42,
            iconRes = R.drawable.ic_toggle,
            type = ComponentDetailedType.Toggle
        ),
        ComponentChoiceData(
            titleId = R.string.s43,
            iconRes = R.drawable.ic_slider,
            type = ComponentDetailedType.Slider
        )
    )

    data class BottomNavData(
        @StringRes val nextButtonText: Int = R.string.next,
        val hasPrevButton: Boolean = false,
    )

    data class ComponentChoiceData(
        @StringRes val titleId: Int,
        @DrawableRes val iconRes: Int,
        val type: ComponentDetailedType,
    )

    data class SettingData(
        @StringRes val title: Int,
        val inputFieldData: InputFieldData
    )

    enum class SettingType {
        Name,
        DefaultValue,
        OnClickSend,
        OnToggleOnSend,
        OnToggleOffSend,
        MaxValue,
        MinValue,
    }

    companion object {
        const val ADD_COMPONENT_SUCCESS_EVENT = "addComponentSuccess"
        const val SUCCESS_TOAST_MESSAGE = "Successfully added new component"
        const val FAILURE_TOAST_MESSAGE = "Could not add new component"
    }
}