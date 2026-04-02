package com.example.tracker.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tracker.ui.glass.LocalPlatformGlassLayer

@Composable
actual fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val glassLayer = LocalPlatformGlassLayer.current
    if (glassLayer != null) {
        BlurButton(
            onClick = onClick,
            blurGlass = glassLayer,
            modifier = modifier,
            surfaceColor = Color.White.copy(alpha = 0.12f),
            tint = Color.White.copy(alpha = 0.06f),
            content = content
        )
    } else {
        Button(
            onClick = onClick,
            modifier = modifier,
            content = content
        )
    }
}


