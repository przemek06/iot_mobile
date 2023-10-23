package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.data.TopicType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddTopicViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddTopicUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy() }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputFieldData = it.inputFieldData.copy(text = text))
        }
    }
    fun selectTopic(topic: TopicType) {
        _uiState.update {
            it.copy(selectedTopic = topic.name)
        }
    }
}