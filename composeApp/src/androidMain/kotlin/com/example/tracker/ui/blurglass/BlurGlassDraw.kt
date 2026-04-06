package com.example.tracker.ui.blurglass

import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.node.requireGraphicsContext
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.tracker.ui.blurglass.glass.GlassLayer
import com.example.tracker.ui.blurglass.highlight.Highlight
import com.example.tracker.ui.blurglass.highlight.HighlightElement
import com.example.tracker.ui.blurglass.shadow.InsetShadow
import com.example.tracker.ui.blurglass.shadow.InsetShadowElement
import com.example.tracker.ui.blurglass.shadow.Shadow
import com.example.tracker.ui.blurglass.shadow.ShadowElement
import com.example.tracker.ui.blurglass.utils.recordLayer

private val isBlurSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
private val DefaultOnDrawBlurGlass: DrawScope.(DrawScope.() -> Unit) -> Unit = { it() }


fun Modifier.drawBlurGlassBase(
    blurGlass: BlurGlass,
    shape: () -> Shape,
    effects: BlurGlassEffect.() -> Unit,
    layer: (GraphicsLayerScope.() -> Unit)? = null,
    exportedGlass: GlassLayer? = null,
    onDrawBehind: (DrawScope.() -> Unit)? = null,
    onDrawBlurGlass: DrawScope.(drawBlurGlass: DrawScope.() -> Unit) -> Unit = DefaultOnDrawBlurGlass,
    onDrawSurface: (DrawScope.() -> Unit)? = null,
    onDrawFront: (DrawScope.() -> Unit)? = null
): Modifier {
    return drawBlurGlass(
        blurGlass = blurGlass,
        shape = shape,
        effects = effects,
        shadow = null,
        insetShadow = null,
        layer = layer,
        exportedGlass = exportedGlass,
        onDrawBehind = onDrawBehind,
        onDrawBlurGlass = onDrawBlurGlass,
        onDrawSurface = onDrawSurface,
        onDrawFront = onDrawFront
    )
}

fun Modifier.drawBlurGlass(
    blurGlass: BlurGlass,
    shape: () -> Shape,
    effects: BlurGlassEffect.() -> Unit,
    shadow: (() -> Shadow?)? = { Shadow.Default },
    highlight: (() -> Highlight?)? = { Highlight.Default },
    insetShadow: (() -> InsetShadow?)? = null,
    layer: (GraphicsLayerScope.() -> Unit)? = null,
    exportedGlass: GlassLayer? = null,
    onDrawBehind: (DrawScope.() -> Unit)? = null,
    onDrawBlurGlass: DrawScope.(drawBlurGlass: DrawScope.() -> Unit) -> Unit = DefaultOnDrawBlurGlass,
    onDrawSurface: (DrawScope.() -> Unit)? = null,
    onDrawFront: (DrawScope.() -> Unit)? = null
): Modifier {
    val memoizedShape = MemoizedShape(shape)
    return this
        .then(
            if(layer != null) {
                Modifier.graphicsLayer(layer)
            } else {
                    Modifier
            }
        )
        .then(
            if (insetShadow != null) {
                InsetShadowElement(
                    memoizedShape = memoizedShape,
                    shadow = insetShadow
                )
            } else {
                Modifier
            }
        )
        .then(
            if (shadow != null) {
                ShadowElement(
                    memoizedShape = memoizedShape,
                    shadow = shadow
                )
            } else {
                Modifier
            }
        )
        .then(
            if (highlight != null) {
                HighlightElement(
                    memoizedShape = memoizedShape,
                    highlight = highlight
                )
            } else {
                Modifier
            }
        )
        .then(
            DrawBlurGlassElement(
                blurGlass = blurGlass,
                memoizedShape = memoizedShape,
                effects = effects,
                layer = layer,
                exportedGlass = exportedGlass,
                onDrawBehind = onDrawBehind,
                onDrawBlurGlass = onDrawBlurGlass,
                onDrawSurface = onDrawSurface,
                onDrawFront = onDrawFront
            )
        )

}

private class DrawBlurGlassElement(
    val blurGlass: BlurGlass,
    val memoizedShape: MemoizedShape,
    val effects: BlurGlassEffect.() -> Unit,
    val layer: (GraphicsLayerScope.() -> Unit)?,
    val exportedGlass: GlassLayer?,
    val onDrawBehind: (DrawScope.() -> Unit)?,
    val onDrawBlurGlass: DrawScope.(drawBlurGlass: DrawScope.() -> Unit) -> Unit,
    val onDrawSurface: (DrawScope.() -> Unit)?,
    val onDrawFront: (DrawScope.() -> Unit)?
) : ModifierNodeElement<DrawBlurGlassNode>() {

    override fun create(): DrawBlurGlassNode {
        return DrawBlurGlassNode(
            blurGlass = blurGlass,
            memoizedShape = memoizedShape,
            effects = effects,
            layer = layer,
            exportedGlass = exportedGlass,
            onDrawBehind = onDrawBehind,
            onDrawBlurGlass = onDrawBlurGlass,
            onDrawSurface = onDrawSurface,
            onDrawFront = onDrawFront
        )
    }

    override fun update(node: DrawBlurGlassNode) {
        node.blurGlass = blurGlass
        node.memoizedShape = memoizedShape
        node.effects = effects
        node.layer = layer
        node.exportedGlass = exportedGlass
        node.onDrawBehind = onDrawBehind
        node.onDrawBlurGlass = onDrawBlurGlass
        node.onDrawSurface = onDrawSurface
        node.onDrawFront = onDrawFront
        node.invalidateDrawCache()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "drawBlurGlass"
        properties["blurGlass"] = blurGlass
        properties["memoizedShape"] = memoizedShape
        properties["effects"] = effects
        properties["layer"] = layer
        properties["exportedGlass"] = exportedGlass
        properties["onDrawBehind"] = onDrawBehind
        properties["onDrawBlurGlass"] = onDrawBlurGlass
        properties["onDrawSurface"] = onDrawSurface
        properties["onDrawFront"] = onDrawFront
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DrawBlurGlassElement) return false

        return blurGlass == other.blurGlass &&
                memoizedShape == other.memoizedShape &&
                effects == other.effects &&
                layer == other.layer &&
                exportedGlass == other.exportedGlass &&
                onDrawBehind == other.onDrawBehind &&
                onDrawBlurGlass == other.onDrawBlurGlass &&
                onDrawSurface == other.onDrawSurface &&
                onDrawFront == other.onDrawFront
    }

    override fun hashCode(): Int {
        var result = blurGlass.hashCode()
        result = 31 * result + memoizedShape.hashCode()
        result = 31 * result + effects.hashCode()
        result = 31 * result + (layer?.hashCode() ?: 0)
        result = 31 * result + (exportedGlass?.hashCode() ?: 0)
        result = 31 * result + (onDrawBehind?.hashCode() ?: 0)
        result = 31 * result + onDrawBlurGlass.hashCode()
        result = 31 * result + (onDrawSurface?.hashCode() ?: 0)
        result = 31 * result + (onDrawFront?.hashCode() ?: 0)
        return result
    }
}

private class DrawBlurGlassNode(
    var blurGlass: BlurGlass,
    var memoizedShape: MemoizedShape,
    var effects: BlurGlassEffect.() -> Unit,
    var layer: (GraphicsLayerScope.() -> Unit)?,
    var exportedGlass: GlassLayer?,
    var onDrawBehind: (DrawScope.() -> Unit)?,
    var onDrawBlurGlass: DrawScope.(drawBlurGlass: DrawScope.() -> Unit) -> Unit,
    var onDrawSurface: (DrawScope.() -> Unit)?,
    var onDrawFront: (DrawScope.() -> Unit)?
): LayoutModifierNode, DrawModifierNode, GlobalPositionAwareModifierNode, ObserverModifierNode, Modifier.Node(){
    private val effectScope = object : BlurGlassEffectImp() {
        override val shape: Shape get() = memoizedShape.innerShape
    }

    private var graphicsLayer: GraphicsLayer? = null

    private val layoutLayer: GraphicsLayerScope.() -> Unit = {
        clip = true
        shape = memoizedShape.shape
        compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
    }

    private var layoutCoordinates: LayoutCoordinates? = null

    private var padding: Float = 0f

    private val recordBlurGlass: DrawScope.() -> Unit = {
        val canvas = drawContext.canvas
        val padding = padding

        if (padding != 0f) canvas.translate(padding, padding)
        onDrawBlurGlass {
            with(blurGlass) {
                drawBlurGlass(
                    density = effectScope,
                    coordinates = layoutCoordinates,
                    layer = layer
                )
            }
        }
        if (padding != 0f) canvas.translate(-padding, -padding)
    }

    private val drawBlurGlassLayer: DrawScope.() -> Unit = {
        val layer = graphicsLayer
        if (layer != null) {
            val padding = padding
            recordLayer(
                node = this@DrawBlurGlassNode,
                layer,
                size = IntSize(
                    size.width.toInt() + padding.toInt() * 2,
                    size.height.toInt() + padding.toInt() * 2
                ),
                block = recordBlurGlass
            )
            layer.topLeft =
                if (padding != 0f) IntOffset(-padding.toInt(), -padding.toInt())
                else IntOffset.Zero
            drawLayer(layer)
        }
    }

    private val drawVisualLayers: DrawScope.() -> Unit = {
        onDrawBehind?.invoke(this)
        drawBlurGlassLayer()
        onDrawSurface?.invoke(this)
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(IntOffset.Zero, layerBlock = layoutLayer)
        }
    }

    override fun ContentDrawScope.draw() {
        if (effectScope.update(this)) updateEffects()

        drawVisualLayers()
        drawContent()
        onDrawFront?.invoke(this)

        exportedGlass?.graphicsLayer?.let { layer ->
            recordLayer(
                node = this@DrawBlurGlassNode,
                layer
            ) {
                drawVisualLayers()
                onDrawFront?.invoke(this)
            }
        }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            if (blurGlass.coordinates) {
                layoutCoordinates = coordinates
            } else {
                if (layoutCoordinates != null) layoutCoordinates = null
            }
            exportedGlass?.layerCoordinates = coordinates
        }
    }

    override fun onObservedReadsChanged() = invalidateDrawCache()

    fun invalidateDrawCache() = observeEffects()

    private fun observeEffects() {
        observeReads { updateEffects() }
    }

    private fun updateEffects() {
        if (!isBlurSupported) return
        effectScope.apply(effects)
        graphicsLayer?.renderEffect = effectScope.renderEffect?.asComposeRenderEffect()
        padding = effectScope.padding
    }

    override fun onAttach() {
        graphicsLayer = requireGraphicsContext().createGraphicsLayer()
        observeEffects()
    }

    override fun onDetach() {
        requireGraphicsContext().let { ctx ->
            graphicsLayer?.let { ctx.releaseGraphicsLayer(it) }
            graphicsLayer = null
        }
        effectScope.reset()
        layoutCoordinates = null
        exportedGlass?.layerCoordinates = null
    }
}

