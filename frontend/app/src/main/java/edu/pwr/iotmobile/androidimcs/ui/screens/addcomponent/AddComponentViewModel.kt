package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.dto.ActionDestinationDTO
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentDto
import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto
import edu.pwr.iotmobile.androidimcs.data.dto.EActionDestinationType
import edu.pwr.iotmobile.androidimcs.data.scopestates.ComponentsListState
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toDto
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository
import edu.pwr.iotmobile.androidimcs.model.repository.IntegrationRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.scope.ScopeID

class AddComponentViewModel(
    private val topicRepository: TopicRepository,
    private val componentRepository: ComponentRepository,
    private val integrationRepository: IntegrationRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddComponentUiState())
    val uiState = _uiState.asStateFlow()

    private var _projectId: Int? = null
    private var _discordKey: String? = null

    fun init(projectId: Int) {
        if (_projectId == null || projectId != _projectId) {
            _uiState.update { it.copy(isLoading = true) }
            _projectId = projectId

            viewModelScope.launch(Dispatchers.Default) {
                _uiState.update {
                    it.copy(
                        inputComponents = generateInputComponents(),
                        outputComponents = generateOutputComponents(),
                        triggerComponents = generateTriggerComponents(),
                        isLoading = false,
                        isError = false
                    )
                }
            }
        }
    }

    private fun String.isNumeric(): Boolean {
        return this.toDoubleOrNull() != null
    }

    private fun Map.Entry<SettingType, SettingData>.getWithErrors(
        componentType: ComponentDetailedType,
    ): InputFieldData {
        val numericFields = listOf(SettingType.MinValue, SettingType.MaxValue, SettingType.OnClickSend)
        val numericComponentTypes = listOf(ComponentDetailedType.Slider, ComponentDetailedType.LineGraph, ComponentDetailedType.SpeedGraph)
        return if (key in numericFields && componentType in numericComponentTypes) {
            if (value.inputFieldData.text.isNotBlank()) {
                value.inputFieldData.copy(
                    isError = !value.inputFieldData.text.isNumeric(),
                    errorMessage = R.string.s69
                )
            } else {
                value.inputFieldData.copy(
                    isError = value.inputFieldData.text.isBlank(),
                    errorMessage = R.string.s66
                )
            }
        } else {
            value.inputFieldData.copy(
                isError = value.inputFieldData.text.isBlank(),
                errorMessage = R.string.s66
            )
        }
    }

    fun checkInputFields() {
        val chosenComponent = _uiState.value.chosenComponentType ?: run {
            _uiState.update { it.copy(isError = true) }
            return
        }

        _uiState.update { ui ->
            ui.copy(
                settings = ui.settings.map {
                    it.key to it.value.copy(
                        inputFieldData = it.getWithErrors(chosenComponent)
                    )
                }.toMap()
            )
        }
    }

    fun navigateNext(scopeID: ScopeID) {
        when (_uiState.value.currentPage) {
            AddComponentPage.ChooseComponent -> {
                if (uiState.value.chosenComponentType == null) {
                    viewModelScope.launch {
                        toast.toast("You must choose a component type.")
                    }
                    return
                }
                val locProjectId = _projectId
                if (locProjectId == null) {
                    _uiState.update { it.copy(isError = true) }
                    return
                }

                viewModelScope.launch {
                    val topics = getFilteredTopics(locProjectId)
                    _uiState.update {
                        it.copy(
                            topics = topics,
                            currentPage = AddComponentPage.ChooseTopic,
                            bottomNavData = getBottomNavData(AddComponentPage.ChooseTopic)
                        )
                    }
                }
            }

            AddComponentPage.ChooseTopic -> {
                if (uiState.value.chosenTopic == null) {
                    viewModelScope.launch {
                        toast.toast("You must choose a topic.")
                    }
                    return
                }
                _uiState.update {
                    it.copy(
                        currentPage = AddComponentPage.Settings,
                        bottomNavData = getBottomNavData(AddComponentPage.Settings),
                        settings = generateSettings()
                    )
                }
            }

            AddComponentPage.Settings -> {
                checkInputFields()
                if (uiState.value.settings.any { it.value.inputFieldData.isError }) {
                    viewModelScope.launch {
                        toast.toast("Some field is missing data.")
                    }
                    return
                }
                if (checkIfChosenComponentDiscord())
                    openDiscord()
                else
                    onConfirmComponent(scopeID)
            }
            AddComponentPage.Additional -> onConfirmComponent(scopeID)
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
            AddComponentPage.Additional ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.Settings,
                    bottomNavData = getBottomNavData(AddComponentPage.Settings)
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

    fun onChooseDiscordChannel(index: Int) {
        val updatedList = uiState.value.discordChannels.mapIndexed { i, item ->
            if (i == index)
                item.copy(isChecked = !item.isChecked)
            else
                item.copy(isChecked = false)
        }
        _uiState.update {
            it.copy(discordChannels = updatedList)
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

    fun handleUri() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (uiState.value.chosenComponentType) {
                ComponentDetailedType.Discord -> {
                    val discordChannels = generateDiscordChannels()
                    if (discordChannels.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true
                            )
                        }
                        return@launch
                    }

                    _uiState.update {
                        it.copy(
                            currentPage = AddComponentPage.Additional,
                            bottomNavData = getBottomNavData(AddComponentPage.Additional),
                            discordChannels = discordChannels,
                            isLoading = false,
                            isError = false
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            currentPage = AddComponentPage.Additional,
                            bottomNavData = getBottomNavData(AddComponentPage.Additional),
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            }
        }
    }

    fun updateTopics() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val projectId = _projectId ?: return@launch
            val topics = getFilteredTopics(projectId)
            _uiState.update {
                it.copy(
                    topics = topics,
                    isLoading = false
                )
            }
        }
    }

    private fun checkIfChosenComponentDiscord() =
        _uiState.value.chosenComponentType == ComponentDetailedType.Discord

    private fun openDiscord() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val discordKey = integrationRepository.getDiscordKey() ?: kotlin.run {
                    _uiState.update { it.copy(
                        isLoading = false,
                        isError = true
                    ) }
                    return@launch
                }
                _discordKey = discordKey

                val discordUrl = integrationRepository.getDiscordUrl(discordKey) ?: kotlin.run {
                    _uiState.update { it.copy(
                        isLoading = false,
                        isError = true
                    ) }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        discordUrl = discordUrl,
                        isLoading = false,
                        isError = false
                    )
                }
                event.event(DISCORD_EVENT)
            } catch (e: Exception) {
                Log.e("Trigger", "Cannot open discord web", e)
                _uiState.update { it.copy(
                    isLoading = false,
                    isError = true
                ) }
            }
        }
    }

    private fun onConfirmComponent(scopeID: ScopeID) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.Default) {
            val data = getComponentDtoData() ?: run {
                toast.toast(FAILURE_TOAST_MESSAGE)
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            val componentListDto = ComponentsListState.getScoped(scopeID)?.componentListDto ?: return@launch

            Log.d("CompList", "componentListDto")
            Log.d("CompList", componentListDto.toString())

            val newComponents = (componentListDto.components + listOf(data))
                .mapIndexed { index, item -> item.copy(index = index) }
            val newDto = componentListDto.copy(components = newComponents)

            Log.d("Comp", "newDto")
            Log.d("Comp", newDto.toString())

            kotlin.runCatching {
                componentRepository.updateComponentList(newDto)
            }.onSuccess {
                event.event(ADD_COMPONENT_SUCCESS_EVENT)
                toast.toast(SUCCESS_TOAST_MESSAGE)
            }.onFailure {
                toast.toast(FAILURE_TOAST_MESSAGE)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }


    private fun getComponentDtoData(): ComponentDto? {
        val locUiState = _uiState.value
        return ComponentDto(
            componentType = locUiState.chosenComponentType?.belongsTo?.name ?: return null,
            type = locUiState.chosenComponentType.name,
            size = locUiState.chosenComponentType.size,
            topic = locUiState.chosenTopic?.toDto(),
            name = locUiState.settings[SettingType.Name]?.inputFieldData?.text,
            onSendValue = locUiState.settings[SettingType.OnClickSend]?.inputFieldData?.text ?: locUiState.settings[SettingType.OnToggleOnSend]?.inputFieldData?.text,
            onSendAlternative = locUiState.settings[SettingType.OnToggleOffSend]?.inputFieldData?.text,
            maxValue = locUiState.settings[SettingType.MaxValue]?.inputFieldData?.text,
            minValue = locUiState.settings[SettingType.MinValue]?.inputFieldData?.text,
            actionDestinationDTO = locUiState.discordChannels.toActionDestinationDTO() ?: getEmailActionDestinationDto(),
            pattern = locUiState.settings[SettingType.Description]?.inputFieldData?.text
        )
    }

    private fun List<DiscordChannel>.toActionDestinationDTO(): ActionDestinationDTO? {
        return ActionDestinationDTO(
            id = null,
            type = EActionDestinationType.DISCORD,
            token = this.firstOrNull { it.isChecked }?.id ?: return null
        )
    }

    private fun getEmailActionDestinationDto(): ActionDestinationDTO? {
        return ActionDestinationDTO(
            type = EActionDestinationType.EMAIL,
            token = uiState.value.settings[SettingType.Title]?.inputFieldData?.text ?: return null
        )
    }

    private suspend fun getFilteredTopics(projectId: Int): List<Topic> {
        val topics = getTopicsForProject(projectId)
        return when (_uiState.value.chosenComponentType) {
            ComponentDetailedType.Slider, ComponentDetailedType.LineGraph, ComponentDetailedType.SpeedGraph ->
                topics.filter { it.dataType in listOf(TopicDataType.FLOAT, TopicDataType.INT) }

            else -> topics
        }
    }

    private suspend fun getTopicsForProject(projectId: Int): List<Topic> {
        kotlin.runCatching {
            topicRepository.getTopicsByProjectId(projectId)
        }.onSuccess { topics ->
            return topics.mapNotNull { it.toTopic() }
        }.onFailure {
            _uiState.update { it.copy(isError = true) }
            return emptyList()
        }
        return emptyList()
    }

    private fun getBottomNavData(page: AddComponentPage) = when (page) {
        AddComponentPage.ChooseComponent -> BottomNavData()
        AddComponentPage.ChooseTopic -> BottomNavData(hasPrevButton = true)
        AddComponentPage.Settings -> {
            if (checkIfChosenComponentDiscord())
                BottomNavData(hasPrevButton = true)
            else
                BottomNavData(
                    nextButtonText = R.string.confirm,
                    hasPrevButton = true
                )
        }
        AddComponentPage.Additional -> BottomNavData(
            nextButtonText = R.string.confirm,
            hasPrevButton = true
        )
    }

    private fun generateSettings(): Map<SettingType, SettingData> {
        val defaultFields = mapOf(
            SettingType.Name to SettingData(
                title = R.string.s39,
                description = R.string.s50,
                inputFieldData = InputFieldData(
                    label = R.string.name,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            )
        )

        val specificFields = when (uiState.value.chosenComponentType) {

            ComponentDetailedType.Button -> mapOf(
                SettingType.OnClickSend to SettingData(
                    title = R.string.s33,
                    description = R.string.s51,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                )
            )

            ComponentDetailedType.Toggle -> mapOf(
                SettingType.OnToggleOnSend to SettingData(
                    title = R.string.s35,
                    description = R.string.s52,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                ),
                SettingType.OnToggleOffSend to SettingData(
                    title = R.string.s36,
                    description = R.string.s53,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    )
                )
            )

            ComponentDetailedType.Slider -> mapOf(
                SettingType.MaxValue to SettingData(
                    title = R.string.s37,
                    description = R.string.s54,
                    inputFieldData = InputFieldData(
                        label = R.string.s34,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                ),
                SettingType.MinValue to SettingData(
                    title = R.string.s38,
                    description = R.string.s55,
                    inputFieldData = InputFieldData(
                        label = R.string.s34,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                )
            )

            ComponentDetailedType.Discord -> mapOf(
                SettingType.Description to SettingData(
                    title = R.string.a_s52,
                    description = R.string.s71,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    ),
                    isDescription = true
                )
            )

            ComponentDetailedType.Email -> mapOf(
                SettingType.Title to SettingData(
                    title = R.string.a_s54,
                    description = R.string.s70,
                    inputFieldData = InputFieldData(
                        label = R.string.a_s54
                    ),
                    isDescription = true
                ),
                SettingType.Description to SettingData(
                    title = R.string.a_s52,
                    description = R.string.s71,
                    inputFieldData = InputFieldData(
                        label = R.string.s34
                    ),
                    isDescription = true
                )
            )

            else -> emptyMap()

        }

        return defaultFields + specificFields
    }

    private suspend fun generateDiscordChannels(): List<DiscordChannel> {
        try {
            val discordKey = _discordKey ?: return emptyList()
            val guildId = integrationRepository.getGuildId(discordKey) ?: kotlin.run {
                return emptyList()
            }
            val channels = integrationRepository.getDiscordChannels(guildId)
            return channels.map {
                it.toDiscordChannel()
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }

    private fun DiscordChannelDto.toDiscordChannel() = DiscordChannel(
        id = id,
        title = name
    )

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

    private fun generateOutputComponents() = listOf(
        ComponentChoiceData(
            titleId = R.string.a_s55,
            iconRes = R.drawable.ic_graph_time,
            type = ComponentDetailedType.LineGraph
        ),
        ComponentChoiceData(
            titleId = R.string.a_s55,
            iconRes = R.drawable.ic_graph_speed,
            type = ComponentDetailedType.SpeedGraph
        ),
    )

    private fun generateTriggerComponents() = listOf(
        ComponentChoiceData(
            titleId = R.string.a_s50,
            iconRes = R.drawable.ic_discord,
            type = ComponentDetailedType.Discord
        ),
        ComponentChoiceData(
            titleId = R.string.a_s53,
            iconRes = R.drawable.ic_mail,
            type = ComponentDetailedType.Email
        ),
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
        @StringRes val description: Int? = null,
        val inputFieldData: InputFieldData,
        val isDescription: Boolean = false
    )

    data class DiscordChannel(
        val id: String,
        val title: String,
        val isChecked: Boolean = false
    )

    enum class SettingType {
        Name,
        OnClickSend,
        OnToggleOnSend,
        OnToggleOffSend,
        MaxValue,
        MinValue,
        Description,
        Title
    }

    companion object {
        const val ADD_COMPONENT_SUCCESS_EVENT = "addComponentSuccess"
        const val SUCCESS_TOAST_MESSAGE = "Successfully added new component"
        const val FAILURE_TOAST_MESSAGE = "Could not add new component"
        const val DISCORD_EVENT = "DISCORD"
    }
}

open class GetWebActivityResultContract : ActivityResultContract<String, Uri?>() {
    @CallSuper
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(input))
    }

    final override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<Uri?>? = null

    final override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }
}