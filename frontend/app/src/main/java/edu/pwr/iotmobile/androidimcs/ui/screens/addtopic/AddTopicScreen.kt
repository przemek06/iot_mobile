package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.extensions.firstUppercaseRestLowercase
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.RadioButtonWithText
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddTopicScreen(navigation: AddTopicNavigation) {

    val viewModel = koinViewModel<AddTopicViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = AddTopicUiInteraction.default(viewModel)

    val context = LocalContext.current
    viewModel.event.CollectEvent(context) {
        navigation.goBack()
    }
    viewModel.toast.CollectToast(context)

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
    Column(Modifier.fillMaxSize()) {
        TopBar(text = stringResource(R.string.add_topic)) {
            navigation.goBack()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimensions.space22)
        ) {
            item {
                Dimensions.space30.HeightSpacer()
            }

            uiState.inputFields.forEach {
                item {
                    Text(
                        text = stringResource(id = it.value.titleId),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Dimensions.space8.HeightSpacer()
                    Text(
                        text = stringResource(id = it.value.descriptionId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Dimensions.space18.HeightSpacer()
                    InputField(
                        text = it.value.inputFieldData.text,
                        label = stringResource(it.value.inputFieldData.label)
                    ) { text ->
                        uiInteraction.onTextChange(
                            type = it.key,
                            text = text
                        )
                    }
                    Dimensions.space30.HeightSpacer()
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.select_data_type),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space10.HeightSpacer()
            }

            items(TopicDataType.values()) {
                RadioButtonWithText(
                    text = it.name.firstUppercaseRestLowercase(),
                    isSelected = uiState.selectedTopic == it,
                    onClick = { uiInteraction.selectTopic(it) },
                )
            }

            item {
                Dimensions.space30.HeightSpacer()
                Box(modifier = Modifier.fillMaxWidth()) {
                    ButtonCommon(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.confirm),
                        width = Dimensions.buttonWidth
                    ) { uiInteraction.addTopic(projectId = navigation.projectId) }
                }
            }
        }
    }
}