package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import android.content.ActivityNotFoundException
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.ErrorBox
import edu.pwr.iotmobile.androidimcs.ui.components.LoadingBox
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

private val BOTTOM_BAR_HEIGHT = 80.dp
private val BOTTOM_BAR_BUTTON_WIDTH = 120.dp

@Composable
fun AddComponentScreen(navigation: AddComponentNavigation) {
    val viewModel: AddComponentViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = AddComponentUiInteraction.default(viewModel)

    navigation.projectId?.let {
        viewModel.init(it)
    }

    val webActivity = rememberLauncherForActivityResult(
        contract = GetWebActivityResultContract(),
        onResult = {
            viewModel.handleUri()
        }
    )

    LaunchedEffect(navigation.isTopicSuccess) {
        viewModel.updateTopics()
    }

    val context = LocalContext.current
    viewModel.event.CollectEvent(context) {
        when (it) {
            AddComponentViewModel.ADD_COMPONENT_SUCCESS_EVENT ->
                navigation.onReturn()

            AddComponentViewModel.DISCORD_EVENT -> {
                try {
                    uiState.discordUrl?.let { url ->
                        webActivity.launch(url)
                    }
                } catch (e: ActivityNotFoundException) {
                    Log.e("Trigger", "Could not start the web activity", e)
                    Toast.makeText(context, "Could not launch Discord", Toast.LENGTH_SHORT).show()
                }
            }

            else -> { /*Nothing*/ }
        }
    }
    viewModel.toast.CollectToast(context)

    ErrorBox(
        isVisible = uiState.isError,
        onReturn = navigation::onReturn
    )
    LoadingBox(isVisible = uiState.isLoading)

    AnimatedVisibility(
        visible = !uiState.isError && !uiState.isLoading,
        enter = fadeIn(initialAlpha = 0.3f),
        exit = fadeOut()
    ) {
        AddComponentScreenContent(uiState, uiInteraction, navigation)
    }
}

@Composable
private fun AddComponentScreenContent(
    uiState: AddComponentUiState,
    uiInteraction: AddComponentUiInteraction,
    navigation: AddComponentNavigation
) {
    NavigationWrapper(uiState, uiInteraction, navigation) {
        when (uiState.currentPage) {
            AddComponentPage.ChooseComponent -> ChooseComponentScreenContent(uiState, uiInteraction)
            AddComponentPage.ChooseTopic -> ChooseTopicScreenContent(uiState, uiInteraction, navigation)
            AddComponentPage.Settings -> SettingsScreenContent(uiState, uiInteraction)
            AddComponentPage.Additional -> AdditionalScreenContent(uiState, uiInteraction)
        }
    }
}

@Composable
private fun NavigationWrapper(
    uiState: AddComponentUiState,
    uiInteraction: AddComponentUiInteraction,
    navigation: AddComponentNavigation,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().testTag("navWrapper")) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.TopStart)
        ) {
            TopBar(
                text = stringResource(id = R.string.add_component),
                onReturn = navigation::onReturn
            )
            Dimensions.space14.HeightSpacer()
            Column(
                modifier = Modifier
                    .padding(start = Dimensions.space22, end = Dimensions.space22, bottom = BOTTOM_BAR_HEIGHT)
            ) {
                content()
                Dimensions.space22.HeightSpacer()
            }
        }
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomStart),
            uiInteraction = uiInteraction,
            navigation = navigation,
            bottomNavData = uiState.bottomNavData
        )
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    uiInteraction: AddComponentUiInteraction,
    navigation: AddComponentNavigation,
    bottomNavData: AddComponentViewModel.BottomNavData
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BOTTOM_BAR_HEIGHT)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space14)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.space22)
            ) {
                if (bottomNavData.hasPrevButton) {
                    ButtonCommon(
                        text = stringResource(id = R.string.s26),
                        width = BOTTOM_BAR_BUTTON_WIDTH,
                        type = ButtonCommonType.Alternative,
                        onClick = uiInteraction::navigatePrevious
                    )
                }
                ButtonCommon(
                    text = stringResource(id = bottomNavData.nextButtonText),
                    width = BOTTOM_BAR_BUTTON_WIDTH,
                    onClick = { uiInteraction.navigateNext(navigation.scopeID) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AndroidIMCSTheme {
        AddComponentScreenContent(
            uiState = AddComponentUiState(),
            uiInteraction = AddComponentUiInteraction.empty(),
            navigation = AddComponentNavigation.empty()
        )
    }
}