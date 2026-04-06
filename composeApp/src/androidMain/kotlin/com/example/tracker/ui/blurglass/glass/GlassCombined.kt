package com.example.tracker.ui.blurglass.glass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Density
import com.example.tracker.ui.blurglass.BlurGlass

@Composable
fun rememberGlassCombined(
    blurGlass1: BlurGlass,
    blurGlass2: BlurGlass
): BlurGlass {
    return remember(blurGlass1, blurGlass2) {
        GlassCombined2(blurGlass1, blurGlass2)
    }
}

@Composable
fun rememberGlassCombined(
    blurGlass1: BlurGlass,
    blurGlass2: BlurGlass,
    blurGlass3: BlurGlass
): BlurGlass {
    return remember(blurGlass1, blurGlass2, blurGlass3) {
        GlassCombined3(blurGlass1, blurGlass2, blurGlass3)
    }
}

@Composable
fun rememberGlassCombined(vararg blurGlass: BlurGlass): BlurGlass {
    return remember(*blurGlass) {
        GlassCombined(*blurGlass)
    }
}

@Immutable
private class GlassCombined2 (
    val blurGlass1: BlurGlass,
    val blurGlass2: BlurGlass
) : BlurGlass {

    override val coordinates: Boolean = blurGlass1.coordinates || blurGlass2.coordinates

    override fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)?
    ) {
        with(blurGlass1) { drawBlurGlass(density, coordinates, layer) }
        with(blurGlass2) { drawBlurGlass(density, coordinates, layer) }
    }
}

@Immutable
private class GlassCombined3 (
    val blurGlass1: BlurGlass,
    val blurGlass2: BlurGlass,
    val blurGlass3: BlurGlass
) : BlurGlass {

    override val coordinates: Boolean = blurGlass1.coordinates || blurGlass2.coordinates || blurGlass3.coordinates

    override fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)?
    ) {
        with(blurGlass1) { drawBlurGlass(density, coordinates, layer) }
        with(blurGlass2) { drawBlurGlass(density, coordinates, layer) }
        with(blurGlass3) { drawBlurGlass(density, coordinates, layer) }
    }
}

@Immutable
private class GlassCombined (
    vararg val blurGlass: BlurGlass
) : BlurGlass {

    override val coordinates: Boolean = blurGlass.any { it.coordinates }

    override fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)?
    ) {
        blurGlass.forEach { glass ->
            with(glass) { drawBlurGlass(density, coordinates, layer) }
        }
    }
}
