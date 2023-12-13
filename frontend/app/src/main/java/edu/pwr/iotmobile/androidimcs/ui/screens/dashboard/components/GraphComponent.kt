package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiState
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun LazyStaggeredGridItemScope.GraphComponent(
    item: ComponentData,
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope,
    graphColor: Color = Color.Green,
    hasLabels: Boolean = true,
    hasDashedLines: Boolean = true
) {
    ComponentWrapper(
        item = item,
        uiState = uiState,
        uiInteraction = uiInteraction,
        onPlaceItem = onPlaceItem,
        coroutineScope = coroutineScope
    ) {
        val data: List<Pair<LocalDateTime, Float>> = item.graphData

        // Title of component
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space4.HeightSpacer()

        if(data.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.s72),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            return@ComponentWrapper
        }

        val verticalSpacing = if(hasLabels) 100f else 0f
        val horizontalSpacing = if(hasLabels) 50f else 0f
        val transparentGraphColor = graphColor.copy(alpha = 0.5f)
        val yUpperValue = (data.maxBy { it.second }.second * 1.2f).roundToInt()
        val yLowerValue = (data.minBy { it.second }.second * 0.8f).roundToInt()

        val xUpperValue = data.maxBy { it.first }.first.plusHours(1).truncatedTo(ChronoUnit.HOURS)
        val xLowerValue = data.minBy { it.first }.first.truncatedTo(ChronoUnit.HOURS)
        val xTimeDifference = ChronoUnit.HOURS.between(xUpperValue, xLowerValue).absoluteValue

        // Get proper text font
        val context = LocalContext.current
        val density = LocalDensity.current
        val font = ResourcesCompat.getFont(context, R.font.readexpro_medium)
        val textPaint = remember(density) {
            Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = Paint.Align.CENTER
                textSize = density.run { 12.sp.toPx() }
                typeface = font
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize().padding(end = Dimensions.space10)
        ) {
            val spacePerHour = (size.width - verticalSpacing) / (data.size + if(data.size == 1) 1 else -1)
            val yStep = (yUpperValue - yLowerValue) / 5
            val xStep = if (xTimeDifference < 5) xTimeDifference else if (xTimeDifference > 10) xTimeDifference else xTimeDifference / 2
            val spaceStep = (size.width - verticalSpacing) / (xStep)

            // Draw the x-axis values
            if(hasLabels) {
                (0..xStep).forEach { i ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            xLowerValue.plusHours(1 * i).formatDate(),
                            verticalSpacing + i * spaceStep,
                            size.height - 5,
                            textPaint
                        )
                    }
                }
                (0..4).forEach { i ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            (yLowerValue + yStep * i).toString(),
                            30f,
                            size.height - horizontalSpacing - i * size.height / 5f,
                            textPaint
                        )
                    }
                }
            }

            var lastX = 0f
            var lastY = 0f

            val strokePath = Path().apply {

                val height = size.height

                for(i in data.indices) {

                    val record = data[i]
                    val ratio = (record.second - yLowerValue) / (yUpperValue - yLowerValue).toFloat()

                    val x = verticalSpacing + i * spacePerHour
                    val y = height - horizontalSpacing - (ratio * height)

                    if(i == 0) {
                        moveTo(x, y)
                    } else {
                        relativeCubicTo(
                            dx1 = spacePerHour / 2f,
                            dy1 = 0f,
                            dx2 = spacePerHour / 2f,
                            dy2 = y - lastY,
                            dx3 = spacePerHour,
                            dy3 = y - lastY
                        )
                    }
                    lastX = x
                    lastY = y
                    if(data.size == 1) {
                        relativeLineTo(size.width - verticalSpacing, 0f)
                        lastX = size.width
                    }
                }
            }
            val fillPath = android.graphics.Path(strokePath.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(lastX, size.height - horizontalSpacing)
                    lineTo(verticalSpacing, size.height - horizontalSpacing)
                    close()
                }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        transparentGraphColor,
                        Color.Transparent
                    ),
                    endY = size.height - horizontalSpacing
                )
            )
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            if(hasDashedLines) {
                val dashedLine = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                val ratio = (data.last().second - yLowerValue) / (yUpperValue - yLowerValue).toFloat()
                drawLine(
                    color = Color.Red,
                    start = Offset(verticalSpacing, size.height - horizontalSpacing - (ratio * size.height)),
                    end = Offset(size.width, size.height - horizontalSpacing - (ratio * size.height)),
                    pathEffect = dashedLine
                )
            }
        }
    }
}

private fun LocalDateTime.formatDate(pattern: String = "HH:mm"): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        this.truncatedTo(ChronoUnit.HOURS).format(formatter)
    } catch (e: Exception) {
        Log.e("LineGraph", "Error while formatting date.", e)
        ""
    }
}