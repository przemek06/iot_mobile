package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.components.Label
import edu.pwr.iotmobile.androidimcs.ui.components.Option
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer

private const val MAX_USERS_IN_LIST = 2

@Composable
fun GroupScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    var isUserListExpanded by remember { mutableStateOf(false) }

    val memberList = if (uiState.members.size > MAX_USERS_IN_LIST && !isUserListExpanded)
        uiState.members.subList(0, MAX_USERS_IN_LIST)
    else uiState.members

    val bottomText = if (isUserListExpanded) R.string.hide else R.string.expand

    LazyColumn {
        item {
            Role(uiState = uiState)
            Dimensions.space40.HeightSpacer()
        }

        item {
            Text(
                text = stringResource(id = R.string.members_with),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space22.HeightSpacer()
        }

        itemsIndexed(memberList) { index, user ->
            Member(
                user = user,
                role = uiState.userProjectRole
            )
            if (index < memberList.lastIndex)
                Dimensions.space10.HeightSpacer()
        }

        if (uiState.members.size > MAX_USERS_IN_LIST) {
            item {
                Dimensions.space10.HeightSpacer()
                Text(
                    modifier = Modifier.clickable { isUserListExpanded = !isUserListExpanded },
                    text = stringResource(id = bottomText),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        item {
            Dimensions.space40.HeightSpacer()
            Text(
                text = stringResource(id = R.string.manage_group),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space22.HeightSpacer()
        }

        items(uiState.userOptionsList) {
            Option(
                text = stringResource(id = it.titleId),
                isBold = it.isBold,
                onClick = it.onClick
            )
        }

        item {
            Dimensions.space30.HeightSpacer()
        }
    }

}

@Composable
private fun Role(uiState: ProjectDetailsUiState) {
    val userRole = uiState.userProjectRole
    Column {
        Text(
            text = stringResource(id = R.string.your_role),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space22.HeightSpacer()
        Text(
            text = userRole.name.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        if (uiState.userRoleDescriptionId != null) {
            Dimensions.space10.HeightSpacer()
            Text(
                text = stringResource(id = uiState.userRoleDescriptionId),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun LazyItemScope.Member(
    user: User,
    role: UserProjectRole,
) {
    if (role == UserProjectRole.Admin)
        AdminMember(user = user)
    else
        UserNameText(user = user)
}

@Composable
private fun AdminMember(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserNameText(user)
        Dimensions.space10.WidthSpacer()
        Label(text = stringResource(id = R.string.admin))
    }
}

@Composable
private fun UserNameText(user: User) {
    Text(
        text = user.displayName,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}