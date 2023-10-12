package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.LightPurple
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DashboardScreen() {
    val viewModel = koinViewModel<DashboardViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = DashboardUiInteraction.default(viewModel)

    DashboardScreenContent(
        uiState =  uiState,
        uiInteraction = uiInteraction
    )
}

@Composable
private fun DashboardScreenContent(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction
) {
    ComponentsList(
        uiState =  uiState,
        uiInteraction = uiInteraction
    )
}

@Composable
private fun ComponentsList(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction
) {
    val list = uiState.components
    val gridState = rememberLazyStaggeredGridState()

    // TODO: get visible items, get position of point of dragged component, calculate the index of item below it
    // TODO: drag - make items darker (?), duplicate selected item, move around after finger

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        columns = StaggeredGridCells.Adaptive(140.dp),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.space8),
        verticalItemSpacing = Dimensions.space8
    ) {
        for (i in 0..list.lastIndex) {
            val item = list.getOrNull(i) ?: break
            val itemSpan =
                if (item.isFullLine) StaggeredGridItemSpan.FullLine
                else StaggeredGridItemSpan.SingleLane

            item(span = itemSpan) {
                Component(
                    item = list[i],
                    index = i,
                    uiInteraction = uiInteraction,
                    onPlaceItem = { uiInteraction.onPlaceDraggedComponent(gridState.layoutInfo.visibleItemsInfo) }
                )
            }
        }
    }
}

@Composable
private fun Component(
    item: ComponentData,
    index: Int,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDragged by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    val zIndex = if (isDragged) 3f else 1f

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    isDragged = true
                    uiInteraction.setDraggedComponentIndex(index)
                }
                is DragInteraction.Stop -> {
                    isDragged = false
                    onPlaceItem()
                    offset = Offset.Zero
                }
                is DragInteraction.Cancel -> {
                    isDragged = false
                    uiInteraction.setDraggedComponentIndex(null)
                    offset = Offset.Zero
                }
            }
        }
    }

    Box(modifier = Modifier
        .zIndex(zIndex)
        .height(item.height)
        .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
        .pointerInput(Unit) {
            var interaction: DragInteraction.Start? = null
            detectDragGestures(
                onDragStart = {
                    coroutineScope.launch {
                        interaction = DragInteraction.Start()
                        interaction?.run {
                            interactionSource.emit(this)
                        }

                    }
                },
                onDrag = { change: PointerInputChange, dragAmount: Offset ->
                    offset += Offset(dragAmount.x, dragAmount.y)
                },
                onDragCancel = {
                    coroutineScope.launch {
                        interaction?.run {
                            interactionSource.emit(DragInteraction.Cancel(this))
                        }
                    }
                },
                onDragEnd = {
                    coroutineScope.launch {
                        interaction?.run {
                            interactionSource.emit(DragInteraction.Stop(this))
                        }
                    }
                }
            )
        }
        .onGloballyPositioned {
            uiInteraction.setAbsolutePosition(
                offset = it.positionInWindow(),
                index = index
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = LightPurple)
            .clip(MaterialTheme.shapes.large)
            .clickable { uiInteraction.onComponentClick(index) }
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = item.text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}