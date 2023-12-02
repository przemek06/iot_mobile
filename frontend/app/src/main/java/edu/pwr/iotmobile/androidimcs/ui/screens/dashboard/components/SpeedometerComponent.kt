package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.components

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentData
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.ComponentWrapper
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardUiInteraction
import kotlinx.coroutines.CoroutineScope
import kotlin.math.min

@Composable
fun LazyStaggeredGridItemScope.Speedometer(
    item: ComponentData,
    uiInteraction: DashboardUiInteraction,
    onPlaceItem: () -> Unit,
    coroutineScope: CoroutineScope
) {
    ComponentWrapper(
        item = item,
        uiInteraction = uiInteraction,
        onPlaceItem = onPlaceItem,
        coroutineScope = coroutineScope
    ) {
        val currentValue = item.currentValue?.toFloatOrNull() ?: 0f

        val density = LocalDensity.current
        val needleBaseSize = with(density) { 1.dp.toPx() }
        val strokeWidth = with(density) { 6.dp.toPx() }
        val textFontSize = with(density) { 16.dp.toPx() }
        val fontPadding = with(density) { 5.dp.toPx() }

        val gaugeDegrees = 180
        val startAngle = 180f
        val ratio = 1.5f

        val max = item.maxValue?.toFloatOrNull() ?: 0f
        val min = item.minValue?.toFloatOrNull() ?: 0f

        val onBackground = MaterialTheme.colorScheme.onBackground
        val needlePaint = remember { Paint().apply { color = onBackground } }
        val context = LocalContext.current
        val font = ResourcesCompat.getFont(context, R.font.readexpro_medium)
        val textPaint = remember {
            android.graphics.Paint().apply {
                color = onBackground.toArgb()
                textSize = textFontSize
                textAlign = android.graphics.Paint.Align.CENTER
                typeface = font
            }
        }

        // brush with color stops, where each color can have custom proportion
        val brush = Brush.horizontalGradient(
            0f to Color.Red,
            0.7f to Color.Yellow,
            1.0f to Color.Green
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            val canvasSize = min(
                constraints.maxWidth,
                ((constraints.maxHeight * ratio) - 2 * textFontSize).toInt()
            )
            val size = Size(canvasSize.toFloat(), canvasSize.toFloat())
            val canvasSizeDp = with(density) { canvasSize.toDp() }
            val w = size.width
            val h = size.height
            val center = Offset(w / 2, h / 2)
            val textY = center.y + textFontSize + fontPadding

            Canvas(
                modifier = Modifier
                    .size(canvasSizeDp)
                    .offset(y = 25.dp),
                onDraw = {

                    /** Gauge implementation */
                    drawArc(
                        brush = brush,
                        startAngle = startAngle,
                        sweepAngle = gaugeDegrees.toFloat(),
                        useCenter = false,
                        size = Size(width = size.width, height = size.width),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    drawIntoCanvas { canvas ->

                        /** Needle implementation */

                        canvas.save()

                        // rotate canvas based on progress, to move the needle
                        canvas.rotate(
                            degrees = currentValue.toAngle(
                                max = max,
                                min = min,
                                angle = 180f
                            ),
                            pivotX = center.x,
                            pivotY = center.y
                        )

                        //draw Needle shape
                        canvas.drawPath(
                            Path().apply {
                                moveTo(center.x, center.x)
                                lineTo(center.x, center.y + needleBaseSize)
                                lineTo(w / 16 , center.y)
                                lineTo(center.x, center.y - 5)
                                close()
                            },
                            needlePaint
                        )

                        canvas.restore()

                        // draw percentage text
                        canvas.nativeCanvas.drawText(
                            currentValue.round(1).toString(),
                            center.x,
                            textY,
                            textPaint
                        )
                        canvas.nativeCanvas.drawText(
                            item.name,
                            center.x,
                            textPaint.fontMetrics.ascent,
                            textPaint
                        )

                        val minStr = min.round(1).toString()
                        val maxStr = max.round(1).toString()
                        canvas.nativeCanvas.drawText(
                            minStr,
                            textPaint.measureText(minStr) / 2,
                            textY,
                            textPaint
                        )
                        canvas.nativeCanvas.drawText(
                            maxStr,
                            w - textPaint.measureText(maxStr) / 2,
                            textY,
                            textPaint
                        )
                    }
                }
            )
        }
    }
}

private fun Float.toAngle(max: Float, min: Float, angle: Float): Float {
    if(this < min)
        return 0f
    else if(this > max)
        return angle
    return angle * ((this - min) / (max - min))
}