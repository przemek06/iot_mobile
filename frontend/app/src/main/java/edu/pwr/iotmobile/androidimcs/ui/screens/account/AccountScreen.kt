package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.StatData
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.Option
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(navigation: AccountNavigation) {

    val viewModel = koinViewModel<AccountViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(navigation)
    }

    AccountScreenContent(
        uiState = uiState,
        uiInteraction = AccountUiInteraction.default(viewModel)
    )
}

@Composable
private fun AccountScreenContent(
    uiState: AccountUiState,
    uiInteraction: AccountUiInteraction
) {
    var isDisplayNameDialogVisible = remember { mutableStateOf(false) }
    var isLogOutDialogVisible = remember { mutableStateOf(false) }
    var isDeleteAccountDialogVisible = remember { mutableStateOf(false) }

    if (isDisplayNameDialogVisible.value) {
        SimpleDialog(
            title = stringResource(id = R.string.enter_new_display_name),
            buttonText1 = stringResource(id = R.string.cancel),
            buttonText2 = stringResource(id = R.string.confirm),
            buttonFunction1 = { isDisplayNameDialogVisible.value = false },
            buttonFunction2 = {
                uiInteraction.setDisplayName(uiState.inputField.text)
                isDisplayNameDialogVisible.value = false
            },
            content = { DisplayNameInputField(uiState = uiState, uiInteraction = uiInteraction) }
        )
    }
    if (isLogOutDialogVisible.value) {
        SimpleDialog(
            title = stringResource(id = R.string.u_sure_logout),
            buttonText1 = stringResource(id = R.string.no),
            buttonText2 = stringResource(id = R.string.yes),
            buttonFunction1 = { uiInteraction.logout() },
            buttonFunction2 = { isLogOutDialogVisible.value = false }
        )
    }
    if (isDeleteAccountDialogVisible.value) {
        SimpleDialog(
            title = stringResource(id = R.string.u_sure_delete_account),
            buttonText1 = stringResource(id = R.string.no),
            buttonText2 = stringResource(id = R.string.yes),
            buttonFunction1 = { uiInteraction.deleteAccount() },
            buttonFunction2 = { isDeleteAccountDialogVisible.value = false },
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
            text = stringResource(id = R.string.account),
            style = MaterialTheme.typography.titleMedium
        )
        Dimensions.space40.HeightSpacer()
        LazyColumn {

            item {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = uiState.user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space22.HeightSpacer()
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.display_name),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = { isDisplayNameDialogVisible.value = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit display name",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }

                Text(
                    text = uiState.user.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space40.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.stats),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space10.HeightSpacer()
            }

            items(uiState.statList) {
                Stat(it)
            }

            item {
                Dimensions.space40.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.manage_account),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space10.HeightSpacer()
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

@Composable
fun Stat(statData: StatData) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id =statData.label),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space10.WidthSpacer()
        Text(
            text = statData.value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun DisplayNameInputField(
    uiState: AccountUiState,
    uiInteraction: AccountUiInteraction
) {
    InputField(
        text = uiState.inputField.text,
        label = stringResource(id = uiState.inputField.label),
        onValueChange = { uiInteraction.onTextChange(it) }
    )
}

@Composable
fun AccountDeletionContent() {
    Text(
        text = stringResource(id = R.string.delete_account_desc),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Dimensions.space10.HeightSpacer()
    Text(
        text = stringResource(id = R.string.delete_account_desc2),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}

