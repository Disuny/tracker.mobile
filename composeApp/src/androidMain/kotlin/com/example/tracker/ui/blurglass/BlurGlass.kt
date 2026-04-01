package com.example.tracker.ui.blurglass

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.graphics.GraphicsLayerScope

interface BlurGlass {
    fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)? = null
    )
}