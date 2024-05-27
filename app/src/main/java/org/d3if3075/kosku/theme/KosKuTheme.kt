package org.d3if3075.kosku.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = Color.Black,
    primaryContainer = Color.White,
    secondary = Color.Blue)

@Composable
fun KosKuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = typography,
        shapes = shapes,
        content = content,

    )
}
