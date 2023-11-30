package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "AddTopicVM"
class AddTopicViewModel(
    private val topicRepository: TopicRepository,
    val event: Event,
    val toast: Toast,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTopicUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(
            inputFields = generateInputs()
        ) }
    }

    fun checkInputFieldData() {
        _uiState.update { ui ->
            ui.copy(
                inputFields = ui.inputFields.map {
                    if (it.key == InputFieldType.UniqueName)
                        it.key to it.value.copy(
                            inputFieldData = it.value.inputFieldData.copy(
                                isError = it.value.inputFieldData.text.isBlank(),
                                errorMessage = R.string.s66
                            )
                        )
                    else it.key to it.value
                }.toMap()
            )
        }
    }

    fun addTopic(projectId: Int?) {
        if (projectId == null) return
        checkInputFieldData()
        val inputFields = _uiState.value.inputFields
        val isInputFieldError = inputFields.values.any { it.inputFieldData.isError }
        val isDataTypeError = _uiState.value.selectedTopic == null
        if (isDataTypeError) {
            viewModelScope.launch {
                toast.toast("Data type cannot be null.")
            }
        }
        if (isInputFieldError || isDataTypeError) return
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            val topicDto = TopicDto(
                name = "",
                uniqueName = inputFields[InputFieldType.UniqueName]?.inputFieldData?.text ?: run {
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                },
                valueType = _uiState.value.selectedTopic ?: run {
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                },
                projectId = projectId
            )

            kotlin.runCatching {
                topicRepository.createTopic(topicDto)
            }.onSuccess { result ->
                when (result) {
                    CreateResult.Success -> {
                        event.event(ADD_TOPIC_SUCCESS_EVENT)
                        toast.toast("Topic added successfully.")
                    }

                    CreateResult.AlreadyExists -> {
                        _uiState.update { ui ->
                            ui.copy(
                                inputFields = ui.inputFields.map {
                                    if (it.key == InputFieldType.UniqueName)
                                        it.key to it.value.copy(
                                            inputFieldData = it.value.inputFieldData.copy(
                                                isError = true,
                                                errorMessage = R.string.s67
                                            )
                                        )
                                    else it.key to it.value
                                }.toMap(),
                                isLoading = false
                            )
                        }
                        return@launch
                    }

                    else -> toast.toast("Failed to add topic.")
                }
            }.onFailure {
                Log.d(TAG, "Add topic error")
                toast.toast("Failed to add topic.")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onTextChange(
        type: InputFieldType,
        text: String
    ) {
        _uiState.update { ui ->
            val input = ui.inputFields[type] ?: return
            val inputField = input.copy(inputFieldData = input.inputFieldData.copy(text = text))
            val newInputFields = ui.inputFields.toMutableMap()
            newInputFields.replace(type, inputField)
            ui.copy(inputFields = newInputFields)
        }
    }

    fun selectTopic(topic: TopicDataType) {
        _uiState.update {
            it.copy(selectedTopic = topic)
        }
    }

    private fun generateInputs() = mapOf(
        InputFieldType.UniqueName to Input(
            titleId = R.string.enter_topic_name,
            descriptionId = R.string.s23,
            inputFieldData = InputFieldData()
        )
    )

    data class Input(
        @StringRes val titleId: Int,
        @StringRes val descriptionId: Int,
        val inputFieldData: InputFieldData
    )

    enum class InputFieldType {
        UniqueName
    }

    companion object {
        const val ADD_TOPIC_SUCCESS_EVENT = "addTopicSuccess"
    }
}