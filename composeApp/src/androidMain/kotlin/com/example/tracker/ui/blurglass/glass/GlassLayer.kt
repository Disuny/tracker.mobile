package com.example.tracker.ui.blurglass.glass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Density
import com.example.tracker.ui.blurglass.BlurGlass
import com.example.tracker.ui.blurglass.utils.InverseLayer

private val DefaultOnDraw: ContentDrawScope.() -> Unit = { drawContent() }

@Composable
fun memoizedLayerGlass(
    graphicsLayer: GraphicsLayer = rememberGraphicsLayer(),
    onDraw: ContentDrawScope.() -> Unit = DefaultOnDraw
): GlassLayer {
    return remember(graphicsLayer, onDraw) {
        GlassLayer(graphicsLayer, onDraw)
    }
}

@Stable
class GlassLayer internal constructor(
    val graphicsLayer: GraphicsLayer,
    internal val onDraw: ContentDrawScope.() -> Unit
) : BlurGlass {
    override val coordinates: Boolean = true

    internal var layerCoordinates: LayoutCoordinates? = null

    private var inverseLayer: InverseLayer? = null

    override fun DrawScope.drawBlurGlass(
        density: Density,
        coordinates: LayoutCoordinates?,
        layer: (GraphicsLayerScope.() -> Unit)?
    ) {
        val coordinates = coordinates ?: return
        val layerCoordinates = layerCoordinates ?: return
        withTransform({
            if (layer != null) {
                with(getInverseLayer()) { inverseTransform(density, layer) }
            }
            val offset =
                try {
                    layerCoordinates.localPositionOf(coordinates)
                } catch (_: Exception) {
                    // TODO: outer transformations lead to wrong position calculation
                    coordinates.positionInWindow() - layerCoordinates.positionInWindow()
                }
            translate(-offset.x, -offset.y)
        }) {
            drawLayer(graphicsLayer)
        }
    }

    private fun getInverseLayer(): InverseLayer {
        return inverseLayer?.apply { reset() }
            ?: InverseLayer().also { inverseLayer = it }
    }
}
