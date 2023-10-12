package edu.pwr.iotmobile.androidimcs.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.pwr.iotmobile.androidimcs.R

private const val DEFAULT_FONT_HEIGHT = 24
private const val DEFAULT_FONT_LETTER_SPACING = 0.5

private val readexProFamily = FontFamily(
    Font(R.font.readexpro_bold, FontWeight.Bold),
    Font(R.font.readexpro_extralight, FontWeight.ExtraLight),
    Font(R.font.readexpro_light, FontWeight.Light),
    Font(R.font.readexpro_medium, FontWeight.Medium),
    Font(R.font.readexpro_regular, FontWeight.Normal),
    Font(R.font.readexpro_semibold, FontWeight.SemiBold),
)

private val DEFAULT_FONT_FAMILY = readexProFamily

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    titleSmall = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    // For buttons
    bodyLarge = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    // For normal text + top bar
    bodyMedium = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    // For input field
    bodySmall = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    labelMedium = TextStyle(
        fontFamily = DEFAULT_FONT_FAMILY,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
)