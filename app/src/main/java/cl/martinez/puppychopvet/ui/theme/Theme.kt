package cl.martinez.puppychopvet.ui.theme

import android.app.Activity
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

// Tema claro
private val LightColorScheme = lightColorScheme(
    primary = OrangeMain,
    onPrimary = Color.White,
    primaryContainer = OrangeLight,
    onPrimaryContainer = DarkGray,

    secondary = BrownDark,
    onSecondary = Color.White,
    secondaryContainer = BrownLight,
    onSecondaryContainer = Color.White,

    tertiary = GreenAccent,
    onTertiary = Color.White,
    tertiaryContainer = GreenLight,
    onTertiaryContainer = DarkGray,

    background = BeigeBackground,
    onBackground = DarkGray,

    surface = OffWhite,
    onSurface = DarkGray,
    surfaceVariant = Color(0xFFFFF8DC),
    onSurfaceVariant = WarmGray,

    outline = WarmGray,
    outlineVariant = Color(0xFFD3C4B0)
)

// Tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = OrangeLight,
    onPrimary = Color(0xFF2B1810),
    primaryContainer = OrangeDark,
    onPrimaryContainer = Color.White,

    secondary = BrownLight,
    onSecondary = Color(0xFF2B1810),
    secondaryContainer = BrownDark,
    onSecondaryContainer = Color.White,

    tertiary = GreenLight,
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = GreenAccent,
    onTertiaryContainer = Color.White,

    background = Color(0xFF1A1410),
    onBackground = Color(0xFFE8E0D5),

    surface = Color(0xFF2B2520),
    onSurface = Color(0xFFE8E0D5),
    surfaceVariant = Color(0xFF3A3530),
    onSurfaceVariant = Color(0xFFD3C4B0),

    outline = Color(0xFF8B7D6B),
    outlineVariant = Color(0xFF5A4F45)
)

@Composable
fun PuppyChopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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