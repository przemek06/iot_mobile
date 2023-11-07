package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.components.ActionOption
import edu.pwr.iotmobile.androidimcs.ui.components.SearchField
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(navigation: SearchNavigation) {

    val viewModel = koinViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = SearchUiInteraction.default(viewModel)

    LaunchedEffect(Unit) {
        viewModel.init(SearchMode.BLOCK_USERS)
    }

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

    if (uiState.isDialogVisible) {
        val alternative = uiState.selectedUser?.let { !uiState.data.alternative(it) } == false
        SimpleDialog(
            title = if (!alternative) { stringResource(
                    uiState.data.dialogTitle,
                    uiState.selectedUser?.displayName ?: "USER_NAME"
                ) } else { stringResource(
                    uiState.data.dialogTitleAlternative,
                    uiState.selectedUser?.displayName ?: "USER_NAME"
                ) },
            onCloseDialog = { uiInteraction.setDialogInvisible() },
            onConfirm = {
                if (!alternative) {
                    uiState.selectedUser?.let {
                        uiState.data.dialogButton2Function(it)
                    }
                } else {
                    uiState.selectedUser?.let {
                        uiState.data.dialogButton2FunctionAlternative(it)
                    }
                }
                uiInteraction.setDialogInvisible()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22),
    ) {

        TopBar(text = stringResource(uiState.data.topBarText)) {
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

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(uiState.searchedUsers) {
                ActionOption(
                    user = it,
                    userProjectRole = UserProjectRole.VIEWER,
                    buttonText = if(!uiState.data.alternative(it)) {
                        stringResource(uiState.data.buttonText)
                    } else {
                        stringResource(uiState.data.buttonTextAlternative)
                    }
                ) {
                    uiInteraction.setSelectedUser(it)
                    uiInteraction.setDialogVisible()
                }
                Dimensions.space10.HeightSpacer()
            }
        }
    }
}

