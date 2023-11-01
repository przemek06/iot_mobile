package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.gray
import org.koin.androidx.compose.koinViewModel

private val BOTTOM_BAR_HEIGHT = 80.dp
private val BOTTOM_BAR_BUTTON_WIDTH = 120.dp

@Composable
fun AddComponentScreen() {
    val viewModel: AddComponentViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = AddComponentInteraction.default(viewModel)

    AddComponentScreenContent(uiState, uiInteraction)
}

@Composable
private fun AddComponentScreenContent(
    uiState: AddComponentUiState,
    uiInteraction: AddComponentInteraction
) {
    NavigationWrapper(uiState, uiInteraction) {
        when (uiState.currentPage) {
            AddComponentPage.ChooseComponent -> ChooseComponentScreenContent(uiState)
            AddComponentPage.ChooseTopic -> ChooseTopicScreenContent(uiState)
            AddComponentPage.Settings -> Text(text = "settings")
        }
    }
}

@Composable
private fun NavigationWrapper(
    uiState: AddComponentUiState,
    uiInteraction: AddComponentInteraction,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.TopStart)
        ) {
            TopBar(
                text = stringResource(id = R.string.add_component),
                onReturn = {/*TODO*/}
            )
            Dimensions.space14.HeightSpacer()
            Column(modifier = Modifier.padding(horizontal = Dimensions.space22)) {
                content()
            }
        }
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomStart),
            uiInteraction = uiInteraction,
            bottomNavData = uiState.bottomNavData
        )
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    uiInteraction: AddComponentInteraction,
    bottomNavData: AddComponentViewModel.BottomNavData
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BOTTOM_BAR_HEIGHT)
            .background(color = MaterialTheme.colorScheme.gray)
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
                    onClick = uiInteraction::navigateNext
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
            uiInteraction = AddComponentInteraction.empty()
        )
    }
}