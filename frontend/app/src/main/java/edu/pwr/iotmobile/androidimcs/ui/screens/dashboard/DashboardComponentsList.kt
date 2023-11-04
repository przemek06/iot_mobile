@file:OptIn(ExperimentalFoundationApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.LightPurple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ComponentsList(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    navigation: DashboardNavigation
) {
    val list = uiState.components
    val gridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    val windowWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val windowHeight = LocalConfiguration.current.screenHeightDp.toFloat()

    val itm = uiState.components.firstOrNull { it.id == uiState.draggedComponentId }

    // TODO: fix autoscroll
    LaunchedEffect(key1 = itm) {
        coroutineScope.launch {
            if (itm == null) return@launch
            val targetAnimationValue =
                if (itm.absolutePosition.y <= 100) -100f
                else if (itm.absolutePosition.y >= windowHeight - 10) 100f
                else 0f

            gridState.animateScrollBy(value = targetAnimationValue)
        }
    }

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22),
        state = gridState,
        columns = StaggeredGridCells.Adaptive(140.dp),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.space8),
        verticalItemSpacing = Dimensions.space8
    ) {
        item(
            key = "firstItem",
            span = StaggeredGridItemSpan.FullLine
        ) {
            Column {
                Dimensions.space30.HeightSpacer()
                if (uiState.userProjectRole != null && uiState.userProjectRole != UserProjectRole.VIEWER) {
                    ButtonCommon(
                        text = stringResource(id = R.string.add_new_component),
                        type = ButtonCommonType.Secondary,
                        onClick = uiInteraction::onAddNewComponent
                    )
                    Dimensions.space30.HeightSpacer()
                }
            }
        }
        for (i in 0..list.lastIndex) {
            val item = list.getOrNull(i) ?: break
            val itemSpan =
                if (item.isFullLine) StaggeredGridItemSpan.FullLine
                else StaggeredGridItemSpan.SingleLane

            item(
                key = item.id,
                span = itemSpan
            ) {
                Component(
                    item = item,
                    uiInteraction = uiInteraction,
                    onPlaceItem = {
                        uiInteraction.onPlaceDraggedComponent(
                            visibleItems = gridState.layoutInfo.visibleItemsInfo,
                            windowWidth = windowWidth
                        )
                    },
                    coroutineScope = coroutineScope
                )
            }
        }
    }
}

@Composable
private fun LazyStaggeredGridItemScope.Component(
    item: ComponentData,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDragged by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    val zIndex = if (isDragged) 3f else 1f

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    isDragged = true
                    uiInteraction.setDraggedComponentId(item.id)
                }
                is DragInteraction.Stop -> {
                    isDragged = false
                    onPlaceItem()
                    offset = Offset.Zero
                }
                is DragInteraction.Cancel -> {
                    isDragged = false
                    uiInteraction.setDraggedComponentId(null)
                    offset = Offset.Zero
                }
            }
        }
    }

    Box(modifier = Modifier
        .animateItemPlacement()
        .zIndex(zIndex)
        .height(item.height)
        .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
        .pointerInput(Unit) {
            var interaction: DragInteraction.Start? = null
            detectDragGesturesAfterLongPress(
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
                id = item.id
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = LightPurple)
            .clip(MaterialTheme.shapes.large)
            .clickable { uiInteraction.onComponentClick(item.id) }
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = item.text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}