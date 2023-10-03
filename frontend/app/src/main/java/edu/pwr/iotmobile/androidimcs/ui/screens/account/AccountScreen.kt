package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.Option
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
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

    LazyColumn {

        item {
            Text(
                text = stringResource(id = R.string.username),
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
            Text(
                text = stringResource(id = R.string.display_name),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
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

@Composable
fun Popup(input: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        // Popup content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Popup Content")
                Dimensions.space22.HeightSpacer()
                Input()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Label") }
    )
}