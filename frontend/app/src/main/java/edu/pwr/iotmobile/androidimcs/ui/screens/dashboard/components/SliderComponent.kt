package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiState
import kotlinx.coroutines.CoroutineScope

@Composable
fun LazyStaggeredGridItemScope.SliderComponent(
    item: ComponentData,
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope,
) {
    val minValue = item.minValue?.toFloat()
    val maxValue = item.maxValue?.toFloat()

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
            if (minValue != null && maxValue != null) {
                Slider(
                    modifier = Modifier.align(Alignment.Center),
                    value = item.currentValue?.toFloat() ?: minValue,
                    valueRange = minValue..maxValue,
                    onValueChange = { f -> uiInteraction.onLocalComponentValueChange(item, f) },
                    onValueChangeFinished = { uiInteraction.onComponentClick(item, item.currentValue ?: item.minValue) }
                )
            }
        }

    }
}