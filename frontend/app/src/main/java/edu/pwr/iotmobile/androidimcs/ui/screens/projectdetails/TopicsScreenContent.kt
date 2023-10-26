package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.Info
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun TopicsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    if (uiState.isDialogVisible) {
        SimpleDialog(
            title = stringResource(R.string.add_new_topic_dialog),
            buttonText1 = stringResource(id = R.string.cancel),
            buttonText2 = stringResource(id = R.string.confirm),
            buttonFunction1 = { uiInteraction.setDialogInvisible() },
            buttonFunction2 = {
                uiInteraction.addNewDashboard(uiState.inputFieldTopic.text)
                uiInteraction.setDialogInvisible()
            }
        ) {
            Text(
                text = stringResource(id = R.string.enter_topic_name_below),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space10.HeightSpacer()
            InputFieldTopics(
                uiState = uiState,
                uiInteraction = uiInteraction
            )
        }
    }
    if(uiState.isInfoVisible) {
        Info(
            title = stringResource(id = R.string.how_to_access_1),
            buttonFunction = { uiInteraction.setInfoInvisible() }
        ) {
            Text(
                text = stringResource(id = R.string.how_to_access_2),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space22.HeightSpacer()
            Text(
                text = stringResource(id = R.string.how_to_access_3),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    LazyColumn {

        if (uiState.userProjectRole != UserProjectRole.View) {
            item {
                ButtonCommon(
                    text = stringResource(id = R.string.how_to_access),
                    type = ButtonCommonType.Alternative
                ) { uiInteraction.setInfoVisible() }
                Dimensions.space10.HeightSpacer()
                ButtonCommon(
                    text = stringResource(id = R.string.add_new_topic),
                    type = ButtonCommonType.Secondary
                ) { uiInteraction.setDialogVisible() }
                Dimensions.space30.HeightSpacer()
            }
        }

        items(uiState.topics) {
            if (uiState.userProjectRole == UserProjectRole.View) {
                NonErasableBlock(
                    primaryText = "Topic 1",
                    secondaryText = "Float",
                )
            } else {
                ErasableBlock(
                    primaryText = "Topic 1",
                    secondaryText = "Float",
                    onErase = {}
                )
            }
            Dimensions.space14.HeightSpacer()
        }
    }
}

@Composable
private fun ErasableBlock(
    primaryText: String,
    secondaryText: String? = null,
    onErase: () -> Unit
) {
    Block(
        text = "Block",
        onClick = {}
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopicTexts(
                modifier = Modifier.align(Alignment.CenterStart),
                primaryText = primaryText,
                secondaryText = secondaryText
            )
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(radius = Dimensions.space40)
                    ) { onErase() },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Erase topic",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}

@Composable
private fun NonErasableBlock(
    primaryText: String,
    secondaryText: String? = null,
) {
    Block(
        text = "Block",
        onClick = {}
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopicTexts(
                modifier = Modifier.align(Alignment.CenterStart),
                primaryText = primaryText,
                secondaryText = secondaryText
            )
        }
    }
}

@Composable
private fun TopicTexts(
    modifier: Modifier = Modifier,
    primaryText: String,
    secondaryText: String? = null,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = primaryText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        secondaryText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun InputFieldTopics(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    InputField(
        text = uiState.inputFieldTopic.text,
        label = stringResource(id = uiState.inputFieldTopic.label),
        onValueChange = { uiInteraction.onTextChangeTopic(it) }
    )
}