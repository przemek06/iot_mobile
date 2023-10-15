package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.ui.components.ActionOption
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.Label
import edu.pwr.iotmobile.androidimcs.ui.components.SearchField
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(navigation: SearchNavigation) {

    val viewModel = koinViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = SearchUiInteraction.default(viewModel)

    SearchScreenContent(
        uiInteraction = uiInteraction,
        uiState = uiState,
        navigation = navigation
    )
}

@Composable
private fun SearchScreenContent(
    uiInteraction: SearchUiInteraction,
    uiState: SearchUiState,
    navigation: SearchNavigation
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22),
    ) {

        TopBar(text = stringResource(id = R.string.change_password)) {
            navigation.goBack()
        }

        Dimensions.space10.HeightSpacer()

        Text(
            text = stringResource(id = R.string.search_screen_1),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Dimensions.space10.HeightSpacer()

        SearchField(
            text = uiState.searchInputFieldData,
            modifier = Modifier.fillMaxWidth()
        ) { uiInteraction.onTextChange(it) }

        Dimensions.space40.HeightSpacer()

        when(uiState.mode) {
            0 -> AddAdmin(
                uiState = uiState,
                uiInteraction = uiInteraction
            )
            1 -> BlockUsers(
                uiState = uiState,
                uiInteraction = uiInteraction
            )
            else -> {}
        }
    }


}

