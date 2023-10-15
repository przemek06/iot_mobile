package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.components.ActionOption
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun BlockUsers(
    uiState: SearchUiState,
    uiInteraction: SearchUiInteraction
) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        items(uiState.users) {
            if (true /*TODO*/ ) {
                ActionOption(
                    user = it,
                    userProjectRole = UserProjectRole.View,
                    buttonText = stringResource(id = R.string.search_screen_3)
                ) { uiInteraction.blockUser(it) }
            } else {
                ActionOption(
                    user = it,
                    userProjectRole = UserProjectRole.View,
                    buttonText = stringResource(id = R.string.search_screen_4)
                ) { uiInteraction.unblockUser(it) }
            }
            Dimensions.space10.HeightSpacer()
        }
    }
}