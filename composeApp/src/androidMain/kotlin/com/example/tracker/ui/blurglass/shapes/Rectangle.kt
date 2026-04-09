package com.example.tracker.ui.blurglass.shapes

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.graphics.Outline

@Immutable
class Rectangle (
    val cornerRadius: Dp,
    override val style: RoundedStyle = RoundedStyle.Continuous
): RoundedShape {
    override fun corners(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): RoundedShape.Corners {
        val radius = with(density) { cornerRadius.toPx() }.fastCoerceIn(0f, size.minDimension * 0.5f)
        return RoundedShape.Corners(
            topLeft = radius,
            topRight = radius,
            bottomRight = radius,
            bottomLeft = radius
        )
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radius = with(density) { cornerRadius.toPx() }.fastCoerceIn(0f, size.minDimension * 0.5f)
        return roundedOutline(
            size = size,
            radius = radius,
            style = style
        )
    }

    override fun copy(style: RoundedStyle) =
        Rectangle(
            cornerRadius = cornerRadius,
            style = style
        )

    fun copy(
        cornerRadius: Dp = this.cornerRadius,
        style: RoundedStyle = this.style
    ) =
        Rectangle(
            cornerRadius = cornerRadius,
            style = style
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rectangle) return false

        if (cornerRadius != other.cornerRadius) return false
        if (style != other.style) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cornerRadius.hashCode()
        result = 31 * result + style.hashCode()
        return result
    }

    override fun toString(): String {
        return "Rectangle(cornerRadius=$cornerRadius, style=$style)"
    }
}