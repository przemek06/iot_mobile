package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChangePassword(navigation: AccountNavigation) {
    //    Log.d("nav", "project id:")
//    navigation.projectId?.let { Log.d("nav", it) }
    val viewModel = koinViewModel<AccountViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ChangePasswordContent(navigation = navigation)
}

@Composable
fun ChangePasswordContent(navigation: AccountNavigation) {

    Column {
        TopBar(
            onReturn = { navigation.openAccount() }
        )
        Column {
            Text(
                text = stringResource(id = R.string.change_password_1),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.change_password_2),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}