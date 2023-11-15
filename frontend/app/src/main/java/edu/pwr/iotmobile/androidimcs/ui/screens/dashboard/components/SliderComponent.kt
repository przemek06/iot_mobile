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
    val minValue = item.minValue?.toFloat()
    val maxValue = item.maxValue?.toFloat()
    val middlePoint = if (minValue != null && maxValue != null)
        maxValue + minValue / 2
    else 0f

    val lastValue = item.topic?.currentValue?.toFloat() ?: middlePoint
    var currentValue by remember { mutableFloatStateOf(lastValue) }

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
            if (minValue != null && maxValue != null) {
                Slider(
                    modifier = Modifier.align(Alignment.Center),
                    value = currentValue,
                    valueRange = minValue..maxValue,
                    onValueChange = { currentValue = it },
                    onValueChangeFinished = { uiInteraction.onComponentClick(item, currentValue) }
                )
            }
        }

    }
}