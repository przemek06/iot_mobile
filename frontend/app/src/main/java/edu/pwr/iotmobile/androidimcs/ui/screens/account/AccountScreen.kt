package edu.pwr.iotmobile.androidimcs.ui.screens.account

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.text.font.FontWeight
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.Option
import edu.pwr.iotmobile.androidimcs.ui.components.SimplePopup
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsNavigation
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(
    navigation: AccountNavigation
) {
//    Log.d("nav", "project id:")
//    navigation.projectId?.let { Log.d("nav", it) }
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
fun AccountScreenContent(
    uiState: AccountUiState,
    uiInteraction: AccountUiInteraction
) {
    var isDisplayNamePopupVisible = remember { mutableStateOf(false) }
    var isLogOutPopupVisible = remember { mutableStateOf(false) }
    var isDeleteAccountPopupVisible = remember { mutableStateOf(false) }

    if (isDisplayNamePopupVisible.value) {
        SimplePopup(
            title = stringResource(id = R.string.enter_new_display_name),
            buttonText1 = "Cancel",
            buttonText2 = "Confirm",
            buttonFunction1 = { isDisplayNamePopupVisible.value = false },
            buttonFunction2 = { isDisplayNamePopupVisible.value = false },
            content = { DisplayNameInputField(uiState = uiState, uiInteraction = uiInteraction) }
        )
    }
    if (isLogOutPopupVisible.value) {
        SimplePopup(
            title = stringResource(id = R.string.u_sure_logout),
            buttonText1 = "No",
            buttonText2 = "Yes",
            buttonFunction1 = { /*TODO*/ },
            buttonFunction2 = { /*TODO*/ }
        )
    }
    if (isDeleteAccountPopupVisible.value) {
        SimplePopup(
            title = stringResource(id = R.string.u_sure_delete_account),
            buttonText1 = "No",
            buttonText2 = "Yes",
            buttonFunction1 = { /*TODO*/ },
            buttonFunction2 = { /*TODO*/ },
            content = { AccountDeletionContent() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
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
                Dimensions.space22.HeightSpacer()

                Text(
                    text = uiState.username,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space40.HeightSpacer()
            }

            item {
                Row(
                    // horizontalArrangement = Alignment.SpaceBetween        dont work
                ) {
                    Text(
                        text = stringResource(id = R.string.display_name),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = { isDisplayNamePopupVisible.value = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit display name",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
                Dimensions.space22.HeightSpacer()

                Text(
                    text = uiState.displayName,
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
                Dimensions.space22.HeightSpacer()

                Stat(text = stringResource(
                    id = R.string.dashboards_total),
                    value = uiState.dashboardsTotal
                )
                Stat(text = stringResource(
                    id = R.string.accessed_dashboards),
                    value = uiState.accessedDashboards
                )
                Stat(text = stringResource(
                    id = R.string.created_topics_groups),
                    value = uiState.createdTopicGroups
                )

                Dimensions.space40.HeightSpacer()
            }

            item {
                Text(
                    text = stringResource(id = R.string.manage_account),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space22.HeightSpacer()
            }

            items(uiState.userOptionList) {
                Option(
                    text = stringResource(id = it.titleId),
                    isBold = it.isBold,
                    onClick = it.onClick
                )
            }
        }
    }
}

@Composable
fun Stat(text: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space10.WidthSpacer()
        Text(
            text = value.toString(),
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

