package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InvitationData
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvitationsScreen(navigation: InvitationsNavigation) {
    val viewModel = koinViewModel<InvitationsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = InvitationsUiInteraction.default(viewModel)

    InvitationsScreenContent(
        uiState = uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
}

@Composable
private fun InvitationsScreenContent(
    uiState: InvitationsUiState,
    uiInteraction: InvitationsUiInteraction,
    navigation: InvitationsNavigation
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(text = stringResource(id = R.string.show_invitations)) {
            navigation.goBack()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimensions.space22)
        ) {
            if (uiState.invitations.isNotEmpty()) {
                LazyColumn {
                    item {
                        Text(
                            text = stringResource(id = R.string.invitations_1),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Dimensions.space22.HeightSpacer()
                    }
                    items(uiState.invitations) {
                        InvitationCard(
                            invitation = it,
                            uiInteraction = uiInteraction
                        )
                        Dimensions.space22.HeightSpacer()
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.invitations_2),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun InvitationCard(
    invitation: InvitationData,
    uiInteraction: InvitationsUiInteraction
) {

    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.is_inviting) + " ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(invitation.projectName)
        }
    }

    Column {
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space22.HeightSpacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
            ButtonCommon(text = stringResource(id = R.string.accept)) {
                uiInteraction.acceptInvitation(invitation.id)
            }
            Dimensions.space22.WidthSpacer()
            ButtonCommon(
                text = stringResource(id = R.string.decline),
                type = ButtonCommonType.Alternative
            ) {
                uiInteraction.declineInvitation(invitation.id)
            }
        }
        Dimensions.space22.HeightSpacer()
        Divider(thickness = Dimensions.space2)
    }
}