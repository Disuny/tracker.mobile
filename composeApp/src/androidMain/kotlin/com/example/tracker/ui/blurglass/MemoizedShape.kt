package com.example.tracker.ui.blurglass

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Immutable
internal class MemoizedShape(val _shape: () -> Shape) {

    private data class MemoizedKey(
        val size: Size,
        val layoutDirection: LayoutDirection,
        val density: Float,
        val fontScale: Float
    )

    private var memoizedShape: Shape? = null
    private var memoizedKey: MemoizedKey? = null
    private var memoizedOutline: Outline? = null

    val innerShape: Shape get() = _shape()

    val shape: Shape = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val currentShape = _shape()
            val currentKey = MemoizedKey(size, layoutDirection, density.density, density.fontScale)

            if (currentShape != memoizedShape || currentKey != memoizedKey) {
                memoizedShape = currentShape
                memoizedKey = currentKey
                memoizedOutline = currentShape.createOutline(size, layoutDirection, density)
            }
            return memoizedOutline!!
        }
    }
}