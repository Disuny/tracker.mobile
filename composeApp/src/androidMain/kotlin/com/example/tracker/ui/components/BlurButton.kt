package com.example.tracker.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

import com.example.tracker.ui.blurglass.BlurGlass
import com.example.tracker.ui.blurglass.drawBlurGlass
import com.example.tracker.ui.blurglass.effects.blur
import com.example.tracker.ui.blurglass.shapes.Capsule

@Composable
fun BlurButton(
    onClick: () -> Unit,
    blurGlass: BlurGlass,
    modifier: Modifier = Modifier,
    interactive: Boolean = true,
    tint: Color = Color.Unspecified,
    surfaceColor: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier
            .drawBlurGlass(
                blurGlass = blurGlass,
                shape = { Capsule() },
                effects = {
                    blur(10f.dp.toPx())
                },
                onDrawSurface = {
                    if (tint.isSpecified) {
                        drawRect(tint, blendMode = BlendMode.Hue)
                        drawRect(tint.copy(alpha = 0.7f))
                    }
                    if (surfaceColor.isSpecified) {
                        drawRect(surfaceColor)
                    } else {
                        drawRect(Color.White.copy(alpha = 0.14f))
                    }
                }
            )
            .clickable(
                interactionSource = null,
                indication = if (interactive) null else LocalIndication.current,
                role = Role.Button,
                onClick = onClick
            )
            .height(48f.dp)
            .padding(horizontal = 16f.dp),
        horizontalArrangement = Arrangement.spacedBy(8f.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}