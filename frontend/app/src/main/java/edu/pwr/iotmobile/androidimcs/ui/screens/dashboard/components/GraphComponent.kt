package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun LazyStaggeredGridItemScope.GraphComponent(
    item: ComponentData,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope,
    graphColor: Color = Color.Green,
    labels: Boolean = true,
    dashedLines: Boolean = true
) {
    ComponentWrapper(
        item = item,
        uiInteraction = uiInteraction,
        onPlaceItem = onPlaceItem,
        coroutineScope = coroutineScope
    ) {
        val data: List<Pair<LocalDateTime, Float>> = listOf(
            Pair(LocalDateTime.of(2000, 12, 11, 20, 20, 32), 40f),
            Pair(LocalDateTime.of(2000, 12, 11, 20, 20, 33), 20f),
            Pair(LocalDateTime.of(2000, 12, 11, 20, 20, 34), 35f),
            Pair(LocalDateTime.of(2000, 12, 11, 20, 20, 35), 100f),
            Pair(LocalDateTime.of(2000, 12, 11, 20, 20, 36), 80f)
        )

        if(data.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = "No data"
            )
            return@ComponentWrapper
        }

        val verticalSpacing = if(labels) 100f else 0f
        val horizontalSpacing = if(labels) 50f else 0f
        val transparentGraphColor = remember {
            graphColor.copy(alpha = 0.5f)
        }
        val upperValue = remember(data) {
            data.maxBy { it.second }.second * 1.2f
        }
        val lowerValue = remember(data) {
            data.minBy { it.second }.second * 0.8f
        }
        val density = LocalDensity.current

        val context = LocalContext.current
        val font = ResourcesCompat.getFont(context, R.font.readexpro_medium)
        val textPaint = remember(density) {
            Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = Paint.Align.CENTER
                textSize = density.run { 12.sp.toPx() }
                typeface = font
            }
        }
        val textFontSize = with(density) { 16.dp.toPx() }
        val onBackground = MaterialTheme.colorScheme.onBackground
        val titleTextPaint = remember {
            android.graphics.Paint().apply {
                color = onBackground.toArgb()
                textSize = textFontSize
                textAlign = android.graphics.Paint.Align.CENTER
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val spacePerHour = (size.width - verticalSpacing) / (data.size + if(data.size == 1) 1 else 0 - 1)
            val priceStep = (upperValue - lowerValue) / 5

            if(labels) {
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        item.name,
                        size.width / 2,
                        - titleTextPaint.ascent(),
                        titleTextPaint
                    )
                }
                data.indices.forEach { i ->
                    val record = data[i]
                    val x = record.first.second.toString()
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            x,
                            if(data.size == 1) (size.width + verticalSpacing) / 2
                                else verticalSpacing + i * spacePerHour - if(i == data.size - 1)
                                    textPaint.measureText(x) else 0f,
                            size.height - 5,
                            textPaint
                        )
                    }
                }
                (0..4).forEach { i ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            (lowerValue + priceStep * i).round(2).toString(),
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
                    val ratio = (record.second - lowerValue).toFloat() / (upperValue - lowerValue).toFloat()

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

            if(dashedLines) {
                val dashedLine = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                val ratio = (data.last().second - lowerValue).toFloat() / (upperValue - lowerValue).toFloat()
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

fun Float.round(decimalPlaces: Int): Float {
    val multiplier = 10.0.pow(decimalPlaces.toDouble())
    return (this * multiplier).roundToInt() / multiplier.toFloat()
}