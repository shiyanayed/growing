package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WarmGoldDark,
    onPrimary = MidnightNavy,
    primaryContainer = GoldContainerDark,
    onPrimaryContainer = WarmGoldDark,
    secondary = Color(0xFF7FA8D6),
    onSecondary = MidnightNavy,
    secondaryContainer = Color(0xFF1B2F47),
    onSecondaryContainer = Color(0xFFD3E3F8),
    tertiary = WarmGoldDark,
    background = MidnightNavy,
    onBackground = TextCreamLight,
    surface = SurfaceDarkNavy,
    onSurface = TextCreamLight,
    surfaceVariant = SurfaceDarkVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = DeepNavy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE5EDF7),
    onPrimaryContainer = DeepNavy,
    secondary = WarmGold,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = OnGoldContainer,
    tertiary = GoldMuted,
    background = ParchmentLight,
    onBackground = TextDeepInk,
    surface = SurfacePureWhite,
    onSurface = TextDeepInk,
    surfaceVariant = SurfaceWarmVariant,
    onSurfaceVariant = TextSecondaryMuted,
    outline = OutlineWarm
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand identity intentional by default
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
