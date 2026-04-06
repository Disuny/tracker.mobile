package com.example.tracker.ui.blurglass.shadow

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.layer.CompositingStrategy
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireGraphicsContext
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Density
import com.example.tracker.ui.blurglass.MemoizedShape
import com.example.tracker.ui.blurglass.utils.clipOutline

internal class InsetShadowElement(
    val memoizedShape: MemoizedShape,
    val shadow: () -> InsetShadow?
) : ModifierNodeElement<InsetShadowNode>() {

    override fun create(): InsetShadowNode {
        return InsetShadowNode(memoizedShape, shadow)
    }

    override fun update(node: InsetShadowNode) {
        node.memoizedShape = memoizedShape
        node.shadow = shadow
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "innerShadow"
        properties["memoizedShape"] = memoizedShape
        properties["shadow"] = shadow
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InsetShadowElement) return false

        if (memoizedShape != other.memoizedShape) return false
        if (shadow != other.shadow) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memoizedShape.hashCode()
        result = 31 * result + shadow.hashCode()
        return result
    }
}

internal class InsetShadowNode(
    var memoizedShape: MemoizedShape,
    var shadow: () -> InsetShadow?
): DrawModifierNode, Modifier.Node() {
    override val shouldAutoInvalidate: Boolean = false

    private var shadowLayer: GraphicsLayer? = null
    private val paint = Paint()
    private var clipPath: Path? = null
    private var prevRadius = Float.NaN

    override fun ContentDrawScope.draw() {
        drawContent()

        val shadow = shadow() ?: return
        val shadowLayer = shadowLayer

        if (shadowLayer != null) {
            val size = size
            val density: Density = this
            val layoutDirection = layoutDirection

            val radius = shadow.radius.toPx()
            val offsetX = shadow.offset.x.toPx()
            val offsetY = shadow.offset.y.toPx()

            val outline = memoizedShape.shape.createOutline(size, layoutDirection, density)
            val clipPath =
                if (outline is Outline.Rounded) {
                    clipPath ?: Path().also { clipPath = it }
                } else {
                    null
                }


            configurePaint(shadow)

            shadowLayer.alpha = shadow.alpha
            shadowLayer.blendMode = shadow.blendMode

            if (prevRadius != radius) {
                shadowLayer.renderEffect =
                    if (radius > 0f) {
                        BlurEffect(radius, radius, TileMode.Decal)
                    } else {
                        null
                    }
                prevRadius = radius
            }

            shadowLayer.record {
                val canvas = drawContext.canvas
                canvas.save()
                canvas.clipOutline(outline, clipPath)
                canvas.drawOutline(outline, paint)
                canvas.translate(offsetX, offsetY)
                canvas.drawOutline(outline, ShadowMaskPaint)
                canvas.translate(-offsetX, -offsetY)
                canvas.restore()
            }

            val canvas = drawContext.canvas
            canvas.save()
            canvas.clipOutline(outline, clipPath)
            drawLayer(shadowLayer)
            canvas.restore()
        }
    }

    private fun DrawScope.configurePaint(shadow: InsetShadow) {
        paint.color = shadow.color
    }

    override fun onAttach() {
        val graphicsContext = requireGraphicsContext()
        shadowLayer =
            graphicsContext.createGraphicsLayer().apply {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    }

    override fun onDetach() {
        val graphicsContext = requireGraphicsContext()
        shadowLayer?.let { layer ->
            graphicsContext.releaseGraphicsLayer(layer)
            shadowLayer = null
        }
    }

    private fun DrawScope.drawMaskedShadow(outline: Outline, layer: GraphicsLayer) {
        val canvas = drawContext.canvas
        canvas.save()
        canvas.clipOutline(outline, clipPath)
        drawLayer(layer)
        canvas.restore()
    }
}

private val ShadowMaskPaint = Paint().apply {
    blendMode = BlendMode.Clear
}

