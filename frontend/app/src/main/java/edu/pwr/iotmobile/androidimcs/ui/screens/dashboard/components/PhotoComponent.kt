package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiState
import kotlinx.coroutines.CoroutineScope

@Composable
fun LazyStaggeredGridItemScope.PhotoComponent(
    item: ComponentData,
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope,
) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .clickable {
                    uiInteraction.onComponentClick(item, null)
                }
        ) {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_camera),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                contentDescription = null,
            )
        }
    }
}