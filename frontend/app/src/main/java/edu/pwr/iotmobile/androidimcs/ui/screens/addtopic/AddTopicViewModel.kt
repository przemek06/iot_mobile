package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
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

    fun addTopic(projectId: Int?) {
        if (projectId == null) return
        val inputFields = _uiState.value.inputFields
        viewModelScope.launch(Dispatchers.Default) {
            val topicDto = TopicDto(
                name = inputFields[InputFieldType.Name]?.inputFieldData?.text ?: return@launch,
                uniqueName = inputFields[InputFieldType.UniqueName]?.inputFieldData?.text ?: return@launch,
                valueType = _uiState.value.selectedTopic ?: return@launch,
                projectId = projectId
            )
            kotlin.runCatching {
                topicRepository.createTopic(topicDto)
            }.onSuccess {
                event.event(ADD_TOPIC_SUCCESS_EVENT)
                toast.toast("Topic added successfully.")
            }.onFailure {
                Log.d(TAG, "Add topic error")
                toast.toast("Failed to add topic")
            }
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
        InputFieldType.Name to Input(
            titleId = R.string.enter_topic_name,
            descriptionId = R.string.s22,
            inputFieldData = InputFieldData()
        ),
        InputFieldType.UniqueName to Input(
            titleId = R.string.s24,
            descriptionId = R.string.s23,
            inputFieldData = InputFieldData(
                label = R.string.s25
            )
        )
    )

    data class Input(
        @StringRes val titleId: Int,
        @StringRes val descriptionId: Int,
        val inputFieldData: InputFieldData
    )

    enum class InputFieldType {
        Name,
        UniqueName
    }

    companion object {
        const val ADD_TOPIC_SUCCESS_EVENT = "addTopicSuccess"
    }
}