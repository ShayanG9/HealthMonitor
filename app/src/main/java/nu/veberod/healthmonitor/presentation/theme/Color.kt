package nu.veberod.healthmonitor.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Red400 = Color(0xFFCF6679)

val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)
val transparent = Color(0x00FFFFFF)

val primary_darkest = Color(0xFF0A0F0D)
val primary_dark = Color(0xFF2E443E)
val primary = Color(0xFF43AF93)
val primary_light = Color(0xFFC3DAC3)
val primary_lightest = Color(0xFFEBEBD3)
val link = Color(0xFF428CE6)

val accent_light = Color(0xFFF39495)
val accent = Color(0xFFF05D5E)
val accent_dark = Color(0xFFBD3C3D)

val grays_darkest = Color(0xFF151B1A)
val grays_darker = Color(0xFF424A47)
val grays_dark = Color(0xFF5F6966)
val grays = Color(0xFF7C8884)
val grays_light = Color(0xFFBBC1BF)
val grays_lighter = Color(0xFF9CA5A2)
val grays_lightest = Color(0xFFF9FAF9)

val negative_darkest = Color(0xFF601818)
val negative_darker = Color(0xFF881b1b)
val negative_dark = Color(0xFFb72020)
val negative = Color(0xFFdb3030)
val negative_light = Color(0xFFe36464)
val negative_lighter = Color(0xFFf5aaaa)
val negative_lightest = Color(0xFFfbe7e7)

val warning_darkest = Color(0xFF5c4813)
val warning_darker = Color(0xFF8c6d1f)
val warning_dark = Color(0xFFcaa53d)
val warning = Color(0xFFf4ca64)
val warning_light = Color(0xFFfae29f)
val warning_lighter = Color(0xFFfdf3d7)
val warning_lightest = Color(0xFFfffcf4)

val positive_darkest = Color(0xFF155239)
val positive_darker = Color(0xFF197741)
val positive_dark = Color(0xFF259d58)
val positive = Color(0xFF38c172)
val positive_light = Color(0xFF74d99f)
val positive_lighter = Color(0xFFa8eec1)
val positive_lightest = Color(0xFFe3fcec)

internal val wearColorPalette: Colors = Colors(
    primary = primary,
    primaryVariant = primary_darkest,
    secondary = accent_light,
    secondaryVariant = accent_dark,
    error = negative,
    onPrimary = white,
    onSecondary = black,
    onError = white
)