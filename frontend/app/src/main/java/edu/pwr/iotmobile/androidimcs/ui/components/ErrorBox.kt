package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.helpers.extensions.conditional
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isFullScreen: Boolean = true,
    onReturn: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = modifier.conditional(isFullScreen) { fillMaxSize() }) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(240.dp),
                    painter = painterResource(id = R.drawable.ic_thinking),
                    contentDescription = "Error image",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
                Dimensions.space22.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.s57),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space22.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.s58),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (onReturn != null) {
                    Dimensions.space30.HeightSpacer()
                    ButtonCommon(text = stringResource(id = R.string.return_text)) {
                        onReturn()
                    }
                }
            }
        }
    }
}