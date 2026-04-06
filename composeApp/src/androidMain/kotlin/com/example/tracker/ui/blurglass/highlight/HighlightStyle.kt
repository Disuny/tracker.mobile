package com.example.tracker.ui.blurglass.highlight

import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Immutable
interface HighlightStyle {
    val color: Color
    val blendMode: BlendMode

    fun DrawScope.createShader(
        shape: Shape,
    ): Shader?

    @Immutable
    data class Default (
        override val color: Color = Color.White.copy(alpha = 0.5f),
        override val blendMode: BlendMode = BlendMode.Plus,
        val angle: Float = 45f,
        val falloff: Float = 1f
    ) : HighlightStyle {

        constructor(
            intensity: Float,
            angle: Float = 45f,
            falloff: Float = 1f
        ) : this(
            color = Color.White.copy(alpha = intensity),
            angle = angle,
            falloff = falloff
        )

        override fun DrawScope.createShader(shape: Shape): Shader? {
            val angleRad = angle * (PI / 180f).toFloat()
            val dx = cos(angleRad)
            val dy = sin(angleRad)

            val center = Offset(size.width / 2f, size.height / 2f)
            val halfDiag = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f

            val start = Offset(center.x - dx * halfDiag, center.y - dy * halfDiag)
            val end   = Offset(center.x + dx * halfDiag, center.y + dy * halfDiag)

            val steps = 12
            val stops  = FloatArray(steps + 1)
            val colors = ArrayList<Color>(steps + 1)

            for (i in 0..steps) {
                val t = i / steps.toFloat()
                val d = (t - 0.5f) * 2f
                val intensity = abs(d).pow(falloff)
                stops[i] = t
                colors.add(color.copy(alpha = color.alpha * intensity))
            }

            return LinearGradientShader(
                from       = start,
                to         = end,
                colors     = colors,
                colorStops = stops.toList(),
                tileMode   = TileMode.Clamp
            )
        }
    }

    companion object {
        @Stable
        val Default: Default = Default()
    }
}