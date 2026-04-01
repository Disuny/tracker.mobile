package com.example.tracker.ui.blurglass.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.util.fastCoerceIn

internal fun roundedOutline(
    size: Size,
    radius: Float,
    style: RoundedStyle
): Outline {
    val width = size.width
    val height = size.height
    val maxRadius = size.minDimension * 0.5f

    return when {
        radius == 0f -> Outline.Rectangle(Rect(0f, 0f, width, height))

        style == RoundedStyle.Circular || (width == height && radius >= maxRadius) -> {
            Outline.Rounded(
                RoundRect(
                    left = 0f, top = 0f, right = width, bottom = height,
                    topLeftCornerRadius = CornerRadius(radius),
                    topRightCornerRadius = CornerRadius(radius),
                    bottomRightCornerRadius = CornerRadius(radius),
                    bottomLeftCornerRadius = CornerRadius(radius)
                )
            )
        }

        else -> Outline.Generic(
            RoundedPath(size, radius)
        )
    }
}

internal fun roundedOutline(
    size: Size,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float,
    style: RoundedStyle
): Outline {
    val width = size.width
    val height = size.height
    val maxRadius = size.minDimension * 0.5f
    val radii = listOf(topLeft, topRight, bottomRight, bottomLeft)

    return when {
        radii.all { it == 0f } -> Outline.Rectangle(Rect(0f, 0f, width, height))

        style == RoundedStyle.Circular || (width == height && radii.all { it >= maxRadius }) -> {
            Outline.Rounded(
                RoundRect(
                    left = 0f, top = 0f, right = width, bottom = height,
                    topLeftCornerRadius = CornerRadius(topLeft),
                    topRightCornerRadius = CornerRadius(topRight),
                    bottomRightCornerRadius = CornerRadius(bottomRight),
                    bottomLeftCornerRadius = CornerRadius(bottomLeft)
                )
            )
        }

        else -> Outline.Generic(
            RoundedPath(size, topLeft, topRight, bottomRight, bottomLeft)
        )
    }
}

private fun RoundedPath(
    size: Size,
    radius: Float,
    path: Path? = null
): Path = RoundedPath(
    size, radius, radius, radius, radius, path
)

private fun RoundedPath(
    size: Size,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float,
    path: Path? = null
): Path {
    val width = size.width.toDouble()
    val height = size.height.toDouble()
    val path = path?.apply { rewind() } ?: Path()

    val cornerBuilder = RoundedBuilder.Default
    val corners = listOf(topRight, bottomRight, bottomLeft, topLeft)

    return path.apply {
        var startX: Double
        var startY: Double

        var isFirstCorner = true

        corners.forEachIndexed { cornerIndex, radiusFloat ->
            val radius = radiusFloat.toDouble()
            val bezierPoints = calculateBezierPoints(width, height, radius, cornerBuilder)
            if (bezierPoints.isEmpty()) return@apply

            val (x, y) = getCornerStartPosition(cornerIndex, width, height, radius)

            if (isFirstCorner) {
                moveTo((x + bezierPoints[0] * radius).toFloat(), (y + bezierPoints[1] * radius).toFloat())
                isFirstCorner = false
            } else {
                lineTo((x + bezierPoints[0] * radius).toFloat(), (y + bezierPoints[1] * radius).toFloat())
            }
            drawCornerCurves(this, x, y, radius, bezierPoints)
        }

        close()
    }
}

private fun calculateBezierPoints(
    width: Double,
    height: Double,
    radius: Double,
    cornerBuilder: RoundedBuilder
): DoubleArray {
    if (radius <= 0) return doubleArrayOf()

    val tW = ((width * 0.5 - radius) / radius).fastCoerceIn(0.0, 1.0)
    val tH = ((height * 0.5 - radius) / radius).fastCoerceIn(0.0, 1.0)
    return cornerBuilder.getCornerBezierPoints(tW, tH)
        .takeIf { it.size >= 20 } ?: doubleArrayOf()
}

private fun getCornerStartPosition(
    cornerIndex: Int,
    width: Double,
    height: Double,
    radius: Double
): Pair<Double, Double> = when (cornerIndex) {
    0 -> Pair(width - radius, 0.0)      // topRight
    1 -> Pair(width - radius, height)   // bottomRight
    2 -> Pair(radius, height)            // bottomLeft
    3 -> Pair(radius, 0.0)               // topLeft
    else -> Pair(0.0, 0.0)
}

private fun drawCornerCurves(
    path: Path,
    x: Double,
    y: Double,
    radius: Double,
    bezierPoints: DoubleArray
) {
    fun pointAt(idx: Int): Pair<Double, Double> = Pair(bezierPoints[idx], bezierPoints[idx + 1])

    repeat(3) { curveIndex ->
        val startIdx = 2 + (curveIndex * 6)
        val (cp1x, cp1y) = pointAt(startIdx)
        val (cp2x, cp2y) = pointAt(startIdx + 2)
        val (endX, endY) = pointAt(startIdx + 4)

        path.cubicTo(
            (x + cp1x * radius).toFloat(), (y + cp1y * radius).toFloat(),
            (x + cp2x * radius).toFloat(), (y + cp2y * radius).toFloat(),
            (x + endX * radius).toFloat(), (y + endY * radius).toFloat()
        )
    }
}