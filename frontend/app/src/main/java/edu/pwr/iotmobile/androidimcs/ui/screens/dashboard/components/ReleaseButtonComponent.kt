package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiState
import kotlinx.coroutines.CoroutineScope

@Composable
fun LazyStaggeredGridItemScope.ReleaseButtonComponent(
    item: ComponentData,
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope,
) {
    var isPressed by remember { mutableStateOf(false) }
    val bgColor: Color by animateColorAsState(
        if (isPressed) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.primary
    )

    ComponentWrapper(
        item = item,
        uiState = uiState,
        uiInteraction = uiInteraction,
        onPlaceItem = onPlaceItem,
        coroutineScope = coroutineScope
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(color = bgColor)
                    .clickable { }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                uiInteraction.onComponentClick(item, item.onSendValue)
                                tryAwaitRelease()
                                isPressed = false
                                uiInteraction.onComponentClick(item, item.onSendAlternativeValue)
                            },
                        )
                    }
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_spiral),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.background),
                    contentDescription = null,
                )
            }
        }
    }
}