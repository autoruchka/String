package com.example.string.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCenterImage(centerPainter: androidx.compose.ui.graphics.painter.Painter) {

    //reminder to do ti later

    val movement = 40.dp
    val density = LocalDensity.current
    val pxMovement = with(density) { movement.toPx() }

    val anim = rememberInfiniteTransition()
    val offset by anim.animateFloat(
        initialValue = -pxMovement,
        targetValue = pxMovement,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        painter = centerPainter,
        contentDescription = "Animated center image",
        modifier = Modifier
            .size(150.dp)
            .offset { IntOffset(offset.toInt(), 0) },
        contentScale = ContentScale.Fit
    )
}