package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.helpers.extensions.firstUppercaseRestLowercase
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.InfoDialog
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun TopicsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    navigation: ProjectDetailsNavigation
) {
    var isInfoDialogVisible by remember { mutableStateOf(false) }
    var dialogTopic by remember { mutableStateOf<Topic?>(null) }
    TopicAccessDialog(
        isVisible = isInfoDialogVisible,
        onCloseDialog = { isInfoDialogVisible = false }
    )
    DeleteTopicDialog(
        isVisible = dialogTopic != null,
        topic = dialogTopic,
        uiState = uiState,
        uiInteraction = uiInteraction,
        onCloseDialog = { dialogTopic = null }
    )
    LazyColumn {
        item {
            Dimensions.space22.HeightSpacer()
        }

        if (uiState.userProjectRole != UserProjectRole.VIEWER) {
            item {
                ButtonCommon(
                    text = stringResource(id = R.string.how_to_access),
                    type = ButtonCommonType.Alternative
                ) { isInfoDialogVisible = true }
                Dimensions.space10.HeightSpacer()
                ButtonCommon(
                    text = stringResource(id = R.string.add_new_topic),
                    type = ButtonCommonType.Secondary
                ) { navigation.openAddTopic() }
                Dimensions.space30.HeightSpacer()
            }
        }

        items(uiState.topics) {
            if (uiState.userProjectRole == UserProjectRole.VIEWER) {
                NonErasableBlock(
                    primaryText = it.name,
                    secondaryText = it.dataType.name.firstUppercaseRestLowercase(),
                )
            } else {
                ErasableBlock(
                    primaryText = it.name,
                    secondaryText = it.dataType.name.firstUppercaseRestLowercase(),
                    onErase = { dialogTopic = it }
                )
            }
            Dimensions.space14.HeightSpacer()
        }
    }
}

@Composable
private fun TopicAccessDialog(
    isVisible: Boolean,
    onCloseDialog: () -> Unit
) {
    if(isVisible) {
        InfoDialog(
            title = stringResource(id = R.string.how_to_access_1),
            onCloseDialog = { onCloseDialog() }
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
}

@Composable
private fun DeleteTopicDialog(
    isVisible: Boolean,
    topic: Topic?,
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    onCloseDialog: () -> Unit
) {
    if(isVisible && topic != null) {
        SimpleDialog(
            title = stringResource(id = R.string.s49, topic.name),
            closeButtonText = stringResource(id = R.string.no),
            confirmButtonText = stringResource(id = R.string.yes),
            isLoading = uiState.isDialogLoading,
            onCloseDialog = onCloseDialog,
            onConfirm = {
                uiInteraction.deleteTopic(topic.id)
                onCloseDialog()
            }
        ) {
            Text(
                text = stringResource(id = R.string.delete_account_desc2) + ".",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
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