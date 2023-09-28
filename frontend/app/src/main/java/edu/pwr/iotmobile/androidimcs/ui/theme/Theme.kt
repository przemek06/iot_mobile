package edu.pwr.iotmobile.androidimcs.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = DarkPurple,
    tertiary = Orange,
    background = DarkGray,
    surface = DarkGray,
    onPrimary = GrizzlyWhite,
    onSecondary = GrizzlyWhite,
    onTertiary = GrizzlyWhite,
    onBackground = GrizzlyWhite,
    onSurface = GrizzlyWhite,
    outline = GrizzlyWhite
)

private val LightColorScheme = lightColorScheme(
    primary = Purple,
    secondary = DarkPurple,
    tertiary = Orange,
    background = GrizzlyWhite,
    surface = GrizzlyWhite,
    onPrimary = GrizzlyWhite,
    onSecondary = GrizzlyWhite,
    onTertiary = GrizzlyWhite,
    onBackground = DarkGray,
    onSurface = DarkGray,
    outline = DarkGray
)

@Composable
fun AndroidIMCSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // TODO: change to true to enable dynamic color
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}