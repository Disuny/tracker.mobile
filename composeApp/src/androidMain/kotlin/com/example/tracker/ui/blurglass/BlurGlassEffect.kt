package com.example.tracker.ui.blurglass

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import android.graphics.RenderEffect

private data class DrawSnapshot(
    val density: Float,
    val fontScale: Float,
    val size: Size,
    val layoutDirection: LayoutDirection
) {
    companion object {
        val Default = DrawSnapshot(
            density = 1f,
            fontScale = 1f,
            size = Size.Unspecified,
            layoutDirection = LayoutDirection.Ltr
        )
    }

    fun matches(scope: DrawScope): Boolean =
        density == scope.density &&
        fontScale == scope.fontScale &&
        size == scope.size &&
        layoutDirection == scope.layoutDirection
}

sealed interface BlurGlassEffect : Density {
    val size: Size
    val layoutDirection: LayoutDirection
    val shape: Shape
    var padding: Float
    var renderEffect: RenderEffect?
}

internal abstract class BlurGlassEffectImp(

): BlurGlassEffect {
    private var snapshot: DrawSnapshot = DrawSnapshot.Default

    override var density: Float
        get() = snapshot.density
        set(value) { snapshot = snapshot.copy(density = value) }

    override var fontScale: Float
        get() = snapshot.fontScale
        set(value) { snapshot = snapshot.copy(fontScale = value) }

    override var size: Size
        get() = snapshot.size
        set(value) { snapshot = snapshot.copy(size = value) }

    override var layoutDirection: LayoutDirection
        get() = snapshot.layoutDirection
        set(value) { snapshot = snapshot.copy(layoutDirection = value) }

    override var padding: Float = 0f
    override var renderEffect: RenderEffect? = null

    fun update(scope: DrawScope): Boolean {
        if (snapshot.matches(scope)) return false
        snapshot = DrawSnapshot(
            density = scope.density,
            fontScale = scope.fontScale,
            size = scope.size,
            layoutDirection = scope.layoutDirection
        )
        return true
    }

    fun apply(effects: BlurGlassEffect.() -> Unit) {
        padding = 0f
        renderEffect = null
        runCatching { effects() }
    }

    fun reset() {
        snapshot = DrawSnapshot.Default
        padding = 0f
        renderEffect = null
    }
}