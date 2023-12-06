package edu.pwr.iotmobile.androidimcs.ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object  Dimensions {
    val none: Dp
        get() = 0.dp
    val space2: Dp
        get() = 2.dp
    val space4: Dp
        get() = 4.dp
    val space8: Dp
        get() = 8.dp
    val space10: Dp
        get() = 10.dp
    val space12: Dp
        get() = 12.dp
    val space14: Dp
        get() = 14.dp
    val space18: Dp
        get() = 18.dp
    val space22: Dp
        get() = 22.dp
    val space26: Dp
        get() = 26.dp
    val space30: Dp
        get() = 30.dp
    val space34: Dp
        get() = 34.dp
    val space40: Dp
        get() = 40.dp
    val space60: Dp
        get() = 60.dp
    val space85: Dp
        get() = 85.dp
    val topBarHeight: Dp
        get() = 45.dp
    val optionItemHeight: Dp
        get() = 50.dp

    val buttonWidth: Dp
        get() = 280.dp
}

@Composable
fun Dp.HeightSpacer() {
    Spacer(modifier = Modifier.height(this))
}

@Composable
fun Dp.WidthSpacer() {
    Spacer(modifier = Modifier.width(this))
}

