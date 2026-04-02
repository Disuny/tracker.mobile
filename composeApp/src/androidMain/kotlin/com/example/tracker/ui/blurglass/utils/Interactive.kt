package com.example.tracker.ui.blurglass.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Interactive (
    val animation: CoroutineScope,
    val position: (size: Size, offset: Offset) -> Offset = { _, offset -> offset }
) {
    private val pressProgressAnimationSpec = spring(0.5f, 300f, 0.001f)
    private val positionAnimationSpec = spring(0.5f, 300f, Offset.VisibilityThreshold)

    private val pressProgressAnimation = Animatable(0f, 0.001f)
    private val positionAnimation = Animatable(Offset.Zero, Offset.VectorConverter, Offset.VisibilityThreshold)

    private var startPosition = Offset.Zero
    val pressProgress: Float get() = pressProgressAnimation.value
    val offset: Offset get() = positionAnimation.value - startPosition

    val modifier: Modifier =
        Modifier.drawWithContent {
            drawContent()

            val progress = pressProgressAnimation.value
            if (progress > 0f) {
                drawRect(
                    Color.White.copy(0.08f * progress),
                    blendMode = BlendMode.Plus
                )

                val position = position(size, positionAnimation.value)
                val radius = size.minDimension * 1.5f
                val constrainedPosition = Offset(
                    position.x.coerceIn(0f, size.width),
                    position.y.coerceIn(0f, size.height)
                )

                val innerRadius = radius * 0.5f
                val colorValue = Color.White.copy(0.15f * progress)

                drawCircle(
                    brush = Brush.radialGradient(
                        colorStops = arrayOf(
                            0f to colorValue,
                            (innerRadius / radius) to colorValue,
                            1f to colorValue.copy(alpha = 0f)
                        ),
                        center = constrainedPosition,
                        radius = radius
                    ),
                    center = constrainedPosition,
                    radius = radius,
                    blendMode = BlendMode.Plus
                )
            }
        }

    val gestureModifier: Modifier =
        Modifier.pointerInput(animation) {
            dragGestures(
                onDragStart = { down ->
                    startPosition = down.position
                    animation.launch {
                        launch { pressProgressAnimation.animateTo(1f, pressProgressAnimationSpec) }
                        launch { positionAnimation.snapTo(startPosition) }
                    }
                },
                onDragEnd = {
                    animation.launch {
                        launch { pressProgressAnimation.animateTo(0f, pressProgressAnimationSpec) }
                        launch { positionAnimation.animateTo(startPosition, positionAnimationSpec) }
                    }
                },
                onDragCancel = {
                    animation.launch {
                        launch { pressProgressAnimation.animateTo(0f, pressProgressAnimationSpec) }
                        launch { positionAnimation.animateTo(startPosition, positionAnimationSpec) }
                    }
                }
            ) { change, _ ->
                animation.launch { positionAnimation.snapTo(change.position) }
            }
        }

}