package com.example.tracker.ui.blurglass.shapes


import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density

@Immutable
sealed interface RoundedShape : Shape {
    val style: RoundedStyle? get() = null

    fun corners(size: Size, layoutDirection: LayoutDirection, density: Density): Corners
    fun copy(style: RoundedStyle): RoundedShape

    data class Corners(
        val topLeft: Float,
        val topRight: Float,
        val bottomRight: Float,
        val bottomLeft: Float
    )
}