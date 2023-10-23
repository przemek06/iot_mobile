package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.TopicType
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddTopicScreen(navigation: AddTopicNavigation) {

    val viewModel = koinViewModel<AddTopicViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = AddTopicUiInteraction.default(viewModel)

    AddTopicScreenContent(
        navigation = navigation,
        uiState = uiState,
        uiInteraction = uiInteraction
    )
}

@Composable
private fun AddTopicScreenContent(
    navigation: AddTopicNavigation,
    uiState: AddTopicUiState,
    uiInteraction: AddTopicUiInteraction
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
        TopBar(text = stringResource(R.string.add_topic)) {
            navigation.goBack()
        }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Dimensions.space85.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.enter_topic_name),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space22.HeightSpacer()
                InputField(
                    text = uiState.inputFieldData.text,
                    label = stringResource(uiState.inputFieldData.label)
                ) { uiInteraction.onTextChange(it) }
                Dimensions.space40.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.select_data_type),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space10.HeightSpacer()
            }
            items(TopicType.values()) {
                TopicRadio(
                    topic = it,
                    uiState = uiState,
                    uiInteraction = uiInteraction
                )
            }
            item {
                Dimensions.space40.HeightSpacer()
                ButtonCommon(
                    text = stringResource(id = R.string.confirm),
                    width = Dimensions.buttonWidth
                ) { }
            }
        }
    }
}

@Composable
private fun TopicRadio(
    topic: TopicType,
    uiState: AddTopicUiState,
    uiInteraction: AddTopicUiInteraction
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = uiState.selectedTopic == topic.name,
            onClick = { uiInteraction.selectTopic(topic) }
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = topic.label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}