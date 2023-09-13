package edu.pwr.iotmobile.androidimcs.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private const val DEFAULT_FONT_HEIGHT = 24
private const val DEFAULT_FONT_LETTER_SPACING = 0.5

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    // For buttons
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    // For normal text + top bar
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    // For input field
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = DEFAULT_FONT_HEIGHT.sp,
        letterSpacing = DEFAULT_FONT_LETTER_SPACING.sp
    ),
)