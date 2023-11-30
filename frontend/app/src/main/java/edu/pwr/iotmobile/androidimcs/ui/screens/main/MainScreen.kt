package edu.pwr.iotmobile.androidimcs.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.ErrorBox
import edu.pwr.iotmobile.androidimcs.ui.components.LoadingBox
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navigation: MainScreenNavigation) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(navigation.isDashboardDeleted) {
        viewModel.updateLastAccessed()
    }

    ErrorBox(isVisible = uiState.isError)
    LoadingBox(isVisible = uiState.isLoading)

    AnimatedVisibility(
        visible = !uiState.isError && !uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        MainScreenContent(
            uiState = uiState,
            navigation = navigation
        )
    }
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
            val userName = uiState.user?.displayName?.let {
                ", $it"
            } ?: ""

            Dimensions.space60.HeightSpacer()
            Text(
                text = stringResource(id = R.string.a_s58) + userName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Dimensions.space30.HeightSpacer()
        }

        if (uiState.dashboards.isEmpty()) {
            item {
                NoDashboards(navigation)
                Dimensions.space30.HeightSpacer()
            }
        }

        item {
            Invitations(
                isVisible = uiState.isInvitation,
                navigation = navigation
            )
            if (uiState.isInvitation)
                Dimensions.space30.HeightSpacer()
        }

        item {
            NewToApp(navigation)
            Dimensions.space30.HeightSpacer()
        }

        item {
            if (uiState.dashboards.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.main_screen_2),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space18.HeightSpacer()
            }
        }

        items(uiState.dashboards) {
            Block(
                text = it.dashboardName,
                onClick = { navigation.openDashboardScreen(it.projectId, it.dashboardId, it.dashboardName) }
            )
            Dimensions.space14.HeightSpacer()
        }

        item {
            Dimensions.space30.HeightSpacer()
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
private fun NewToApp(
    navigation: MainScreenNavigation
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.a_s59),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space4.HeightSpacer()
            Text(
                text = stringResource(id = R.string.a_s61),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Dimensions.space22.HeightSpacer()
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(id = R.drawable.ic_dog_cosmos),
            contentDescription = "Dog in cosmos",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
        )
        Dimensions.space10.HeightSpacer()
        ButtonCommon(
            text = stringResource(id = R.string.learn),
            onClick = navigation::openLearn
        )
    }
}

@Composable
private fun NoDashboards(
    navigation: MainScreenNavigation
) {
    Column {
        Text(
            text = stringResource(id = R.string.a_s62),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space14.HeightSpacer()
        Text(
            text = stringResource(id = R.string.a_s60),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space22.HeightSpacer()
        Box(modifier = Modifier.fillMaxWidth()) {
            ButtonCommon(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.a_s63),
                type = ButtonCommonType.Secondary
            ) { navigation.openProjects() }
        }
    }
}