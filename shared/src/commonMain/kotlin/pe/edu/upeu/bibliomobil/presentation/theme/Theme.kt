package pe.edu.upeu.bibliomobil.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta basada en Azul Oceánico / Libro Náutico (Evitando verde y morado)
private val AzulPrimarioLight = Color(0xFF0061A4)
private val OnAzulPrimarioLight = Color(0xFFFFFFFF)
private val PrimarioContainerLight = Color(0xFFD1E4FF)
private val OnPrimarioContainerLight = Color(0xFF001D36)

private val AzulPrimarioDark = Color(0xFF9ECAFF)
private val OnAzulPrimarioDark = Color(0xFF003258)
private val PrimarioContainerDark = Color(0xFF00497D)
private val OnPrimarioContainerDark = Color(0xFFD1E4FF)

// Esquema de Color Claro Completo (incluyendo roles Material 3 modernos)
val LightColorScheme = lightColorScheme(
    primary = AzulPrimarioLight,
    onPrimary = OnAzulPrimarioLight,
    primaryContainer = PrimarioContainerLight,
    onPrimaryContainer = OnPrimarioContainerLight,
    secondary = Color(0xFF535F70),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7E3F7),
    onSecondaryContainer = Color(0xFF101C2B),
    tertiary = Color(0xFF6B5778),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251431),
    background = Color(0xFFFDFCFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFCFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFF73777F),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    // Roles modernos de contenedores de superficie requeridos
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF3F3FA),
    surfaceContainer = Color(0xFFEEEEF5),
    surfaceContainerHigh = Color(0xFFE8E8EF),
    surfaceContainerHighest = Color(0xFFE2E2E9)
)

// Esquema de Color Oscuro Completo (incluyendo roles Material 3 modernos)
val DarkColorScheme = darkColorScheme(
    primary = AzulPrimarioDark,
    onPrimary = OnAzulPrimarioDark,
    primaryContainer = PrimarioContainerDark,
    onPrimaryContainer = OnPrimarioContainerDark,
    secondary = Color(0xFFBBC7DB),
    onSecondary = Color(0xFF253140),
    secondaryContainer = Color(0xFF3B4858),
    onSecondaryContainer = Color(0xFFD7E3F7),
    tertiary = Color(0xFFD6BEE4),
    onTertiary = Color(0xFF3B2948),
    tertiaryContainer = Color(0xFF523F5F),
    onTertiaryContainer = Color(0xFFF2DAFF),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7D2),
    outline = Color(0xFF8D9199),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    // Roles modernos de contenedores de superficie requeridos
    surfaceContainerLowest = Color(0xFF0C0E10),
    surfaceContainerLow = Color(0xFF1E2022),
    surfaceContainer = Color(0xFF222426),
    surfaceContainerHigh = Color(0xFF2D2F31),
    surfaceContainerHighest = Color(0xFF383A3C)
)

@Composable
fun BiblioMobilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
