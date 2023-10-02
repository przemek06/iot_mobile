package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsScreenContent
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen() {
    val viewModel = koinViewModel<AccountViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    AccountScreenContent(
        uiState = uiState,
        uiInteraction = AccountUiInteraction.default(viewModel)
    )
}

@Composable
fun AccountScreenContent(
    uiState: AccountUiState,
    uiInteraction: AccountUiInteraction
) {


}