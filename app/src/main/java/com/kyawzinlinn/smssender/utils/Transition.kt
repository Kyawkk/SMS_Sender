package com.kyawzinlinn.smssender.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

object Transition {
    val enter = fadeIn() + scaleIn(initialScale = 0.8f, animationSpec = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    ))

    val exit = fadeOut() + scaleOut(targetScale = 1f, animationSpec = tween(300))
}