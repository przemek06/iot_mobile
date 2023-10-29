package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.ui.theme.AndroidIMCSTheme
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    menuItems: List<MenuOption> = emptyList(),
    text: String? = null,
    padding: Dp = Dimensions.none,
    onReturn: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(2f)
            .padding(horizontal = padding)
            .height(Dimensions.topBarHeight)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onReturn
        ) {
            Image(
                modifier = Modifier.align(Alignment.Center),
                alignment = Alignment.Center,
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Return",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }
        text?.let {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Dropdown menu
        if (menuItems.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd)
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { expanded = !expanded }
                ) {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        alignment = Alignment.Center,
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Return",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
                DropdownMenu(
                    modifier = Modifier.align(Alignment.TopEnd),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(id = it.titleId),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            onClick = it.onClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    AndroidIMCSTheme {
        TopBar() {

        }
    }
}