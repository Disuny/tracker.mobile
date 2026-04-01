package com.example.tracker.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Density
import com.example.tracker.ui.blurglass.BlurGlass

@Composable
actual fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    BlurButton(
        onClick = onClick,
        blurGlass = DefaultBlurGlass,
        modifier = modifier,
        content = content
    )
}

private object DefaultBlurGlass : BlurGlass {
    override fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)?
    ) {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.24f),
                    Color.White.copy(alpha = 0.08f)
                )
            )
        )
    }
}
