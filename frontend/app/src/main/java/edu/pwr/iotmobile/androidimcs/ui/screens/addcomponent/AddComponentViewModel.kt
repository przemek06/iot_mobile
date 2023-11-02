package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val event: Event,
    val toast: Toast
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddComponentUiState())
    val uiState = _uiState.asStateFlow()

    private var _projectId: Int? = null

    // TODO: assign data from input fields to component data upon confirm click & send to backend

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

    fun navigateNext() {
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
                { /*TODO: add new component*/ }
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

        val specificFields = when (uiState.value.newComponent.type) {

            ComponentType.Button -> mapOf(
                SettingType.OnClickSend to SettingData(
                    title = R.string.s33,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                )
            )

            ComponentType.Toggle -> mapOf(
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

            ComponentType.Slider -> mapOf(
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
            type = ComponentType.Button
        ),
        ComponentChoiceData(
            titleId = R.string.s42,
            iconRes = R.drawable.ic_toggle,
            type = ComponentType.Toggle
        ),
        ComponentChoiceData(
            titleId = R.string.s43,
            iconRes = R.drawable.ic_slider,
            type = ComponentType.Slider
        )
    )

    data class BottomNavData(
        @StringRes val nextButtonText: Int = R.string.next,
        val hasPrevButton: Boolean = false,
    )

    data class ComponentChoiceData(
        @StringRes val titleId: Int,
        @DrawableRes val iconRes: Int,
        val type: ComponentType
    )

    data class SettingData(
        @StringRes val title: Int,
        val inputFieldData: InputFieldData
    )

    data class ComponentData(
        val type: ComponentType? = null,
        val topic: Topic? = null,
        val name: String = "",
        val defaultValue: Any? = null,
        val onSendValue: Any? = null,
        val onSendAlternativeValue: Any? = null,
        val maxValue: Any? = null,
        val minValue: Any? = null,
    )

    enum class ComponentType {
        Button,
        Toggle,
        Slider,
        Graph,
//        LineGraph,
//        SpeedGraph
    }

    enum class SettingType {
        Name,
        DefaultValue,
        OnClickSend,
        OnToggleOnSend,
        OnToggleOffSend,
        MaxValue,
        MinValue,
    }
}