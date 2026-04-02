package com.example.tracker.ui.blurglass.glass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.example.tracker.ui.blurglass.BlurGlass
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Density

@Composable
fun memoizedGlass(
    blurGlass: BlurGlass,
    onDraw: DrawScope.(drawBackdrop: DrawScope.() -> Unit) -> Unit
): BlurGlass {
    return remember(blurGlass, onDraw) {
        Glass(blurGlass, onDraw)
    }
}

@Immutable
private class Glass(
    val blurGlass: BlurGlass,
    val onDraw: DrawScope.(drawBlurGlass: DrawScope.() -> Unit) -> Unit
) : BlurGlass {
    override val coordinates: Boolean = blurGlass.coordinates

    override fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)?
    ) {
        onDraw { with(blurGlass) { drawBlurGlass(density, coordinates, layer) } }
    }
}
