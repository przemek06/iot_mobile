package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.Option
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountDeletionContent
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminScreen(navigation: AdminNavigation) {

    val viewModel = koinViewModel<AdminViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(navigation)
    }

    AdminScreenContent(uiState)
}

@Composable
fun AdminScreenContent(uiState: AdminUiState) {

    val isLogOutDialogVisible = remember { mutableStateOf(false) }
    val isDeleteAccountDialogVisible = remember { mutableStateOf(false) }

    if (isLogOutDialogVisible.value) {
        SimpleDialog(
            title = stringResource(id = R.string.u_sure_logout),
            closeButtonText = stringResource(id = R.string.no),
            confirmButtonText = stringResource(id = R.string.yes),
            onCloseDialog = { isLogOutDialogVisible.value = false },
            onConfirm = { isLogOutDialogVisible.value = false }
        )
    }
    if (isDeleteAccountDialogVisible.value) {
        SimpleDialog(
            title = stringResource(id = R.string.u_sure_delete_account),
            closeButtonText = stringResource(id = R.string.no),
            confirmButtonText = stringResource(id = R.string.yes),
            onCloseDialog = { isDeleteAccountDialogVisible.value = false },
            onConfirm = { isDeleteAccountDialogVisible.value = false },
            content = { AccountDeletionContent() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
        Dimensions.space40.HeightSpacer()
        Text(
            text = stringResource(id = R.string.admin_1),
            style = MaterialTheme.typography.titleMedium
        )
        Dimensions.space40.HeightSpacer()

        LazyColumn {
            item {
                Text(
                    text = stringResource(id = R.string.admin_2),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(uiState.adminOptions) {
                Option(
                    text = stringResource(id = it.titleId),
                    isBold = it.isBold,
                    onClick = it.onClick
                )
            }

            item {
                Dimensions.space40.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.manage_account),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Option(
                    text = stringResource(id = uiState.changePasswordOption.titleId),
                    isBold = uiState.changePasswordOption.isBold,
                    onClick = uiState.changePasswordOption.onClick
                )
            }
            item {
                Option(
                    text = stringResource(id = R.string.log_out),
                    onClick = { isLogOutDialogVisible.value = true }
                )
            }
            item {
                Option(
                    text = stringResource(id = R.string.delete_account),
                    onClick = { isDeleteAccountDialogVisible.value = true }
                )
            }
        }
    }
}