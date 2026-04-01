package com.example.tracker.ui.blurglass.shadow

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp as lerpDp
import androidx.compose.ui.util.lerp as lerpFloat

@Immutable
data class InsetShadow(
    val radius: Dp = 24.dp,
    val offset: DpOffset = DpOffset(0.dp, radius),
    val color: Color = Color.Black.copy(alpha = 0.15f),
    val alpha: Float = 1f,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode
) {
    companion object {
        @Stable
        val Default = InsetShadow()
    }
}

@Stable
fun InsetShadow.lerpTo(stop: InsetShadow, fraction: Float): InsetShadow =
    InsetShadow(
        radius = lerpDp(radius, stop.radius, fraction),
        offset = lerpDp(offset, stop.offset, fraction),
        color = lerpColor(color, stop.color, fraction),
        alpha = lerpFloat(alpha, stop.alpha, fraction),
        blendMode = if (fraction < 0.5f) blendMode else stop.blendMode
    )
