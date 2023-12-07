package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer

@Composable
fun ActionOption(
    user: User,
    userProjectRole: UserProjectRole,
    buttonText: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EmailWrapper(user) {
            if (userProjectRole == UserProjectRole.ADMIN)
                AdminMember(user = user)
            else
                UserNameText(user = user)
        }
        ButtonCommon(
            text = buttonText,
            type = ButtonCommonType.Alternative
        ) { onClick() }
    }
}

@Composable
private fun AdminMember(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserNameText(user = user)
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

@Composable
private fun EmailWrapper(
    user: User,
    content: @Composable () -> Unit
) {
    Column {
        content()
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}