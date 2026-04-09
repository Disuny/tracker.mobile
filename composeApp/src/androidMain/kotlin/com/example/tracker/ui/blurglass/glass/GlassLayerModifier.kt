package com.example.tracker.ui.blurglass.glass

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import com.example.tracker.ui.blurglass.utils.recordLayer

fun Modifier.layerGlass(glass: GlassLayer): Modifier = this then GlassLayerElement(glass)

private class GlassLayerElement(
    val glass: GlassLayer
) : ModifierNodeElement<GlassLayerNode>() {

    override fun create(): GlassLayerNode {
        return GlassLayerNode(glass)
    }

    override fun update(node: GlassLayerNode) {
        node.glass = glass
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "layerGlass"
        properties["glass"] = glass
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GlassLayerElement) return false

        if (glass != other.glass) return false

        return true
    }

    override fun hashCode(): Int {
        return glass.hashCode()
    }
}

private class GlassLayerNode(
    var glass: GlassLayer
) : DrawModifierNode, GlobalPositionAwareModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        drawContent()
        recordLayer(
            node = this@GlassLayerNode,
            glass.graphicsLayer
        ) {
            glass.onDraw(this@draw)
        }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            glass.layerCoordinates = coordinates
        }
    }

    override fun onDetach() {
        glass.layerCoordinates = null
    }
}