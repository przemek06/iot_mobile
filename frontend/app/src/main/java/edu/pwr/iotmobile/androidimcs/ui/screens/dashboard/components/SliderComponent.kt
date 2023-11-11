package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiState
import kotlinx.coroutines.CoroutineScope

@Composable
fun LazyStaggeredGridItemScope.SliderComponent(
    uiState: DashboardUiState,
    item: ComponentData,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope,
) {
    // TODO: get current value from other place than topic
    val value = item.topic?.currentValue ?: item.onSendValue

    ComponentWrapper(
        item = item,
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
            value?.let { value ->
                var currentValue by remember { mutableFloatStateOf(value as Float) }
                Slider(
                    modifier = Modifier.align(Alignment.Center),
                    value = currentValue,
                    onValueChange = { currentValue = it } ,
                    onValueChangeFinished = { uiInteraction.onComponentClick(item, currentValue) }
                )
            }
        }

    }
}