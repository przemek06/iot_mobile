package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChangePassword(navigation: AccountNavigation) {
    //    Log.d("nav", "project id:")
//    navigation.projectId?.let { Log.d("nav", it) }
    val viewModel = koinViewModel<AccountViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ChangePasswordContent(
        navigation = navigation,
        uiState = uiState,
        uiInteraction = AccountUiInteraction.default(viewModel)
    )
}

@Composable
fun ChangePasswordContent(
    navigation: AccountNavigation,
    uiState: AccountUiState,
    uiInteraction: AccountUiInteraction
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
        TopBar(
            onReturn = { navigation.openAccount() }
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.change_password_1),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Dimensions.space10.HeightSpacer()
            InputField(
                text = uiState.inputFieldPassword.text,
                label = stringResource(id = uiState.inputFieldPassword.label),
                onValueChange = { uiInteraction.onTextChangePassword(it) }
            )
            Dimensions.space40.HeightSpacer()
            Text(
                text = stringResource(id = R.string.change_password_2),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Dimensions.space10.HeightSpacer()
            InputField(
                text = uiState.inputFieldPasswordNew.text,
                label = stringResource(id = uiState.inputFieldPasswordNew.label),
                onValueChange = { uiInteraction.onTextChangePasswordNew(it) }
            )
        }
    }
}