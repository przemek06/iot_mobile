package edu.pwr.iotmobile.androidimcs.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navigation: MainScreenNavigation) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // TODO: Loading and error screens and animation
    MainScreenContent(
        uiState = uiState,
        navigation = navigation
    )
}

@Composable
private fun MainScreenContent(
    uiState: MainScreenUiState,
    navigation: MainScreenNavigation
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimensions.space22)
    ) {
        item {
            Dimensions.space60
            Text(
                text = stringResource(id = R.string.a_s58, uiState.user?.displayName ?: ""),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space30.HeightSpacer()
        }

        item {
            Invitations(
                isVisible = uiState.isInvitation,
                navigation = navigation
            )
        }


    }
}

@Composable
private fun Invitations(
    isVisible: Boolean,
    navigation: MainScreenNavigation
) {
    if (isVisible) {
        Column {
            Text(
                text = stringResource(id = R.string.a_s57),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space22.HeightSpacer()
            Box(modifier = Modifier.fillMaxWidth()) {
                ButtonCommon(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.your_invitations_3),
                    type = ButtonCommonType.Secondary
                ) { navigation.openInvitations() }
            }
        }
    }
}

@Composable
private fun NewToApp() {
    Column {
        Text(
            text = stringResource(id = R.string.a_s57),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}