package com.example.cp3406_assessment_younghoocheon.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme
import kotlinx.coroutines.delay

private const val FADE_IN_MILLIS = 700
private const val HOLD_MILLIS = 900L

/**
 * Branded intro shown on launch: red speed streaks behind the F1 logo.
 * Calls [onFinished] once the animation has played; the caller decides how to dismiss it.
 */
@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(FADE_IN_MILLIS, easing = FastOutSlowInEasing))
        delay(HOLD_MILLIS)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            // The splash sits on top of the app, so swallow touches until it is gone.
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        SpeedStreaks(modifier = Modifier.fillMaxSize())
        // Contains only the logo, shown exactly as designed: it fades in while scaling up slightly.
        Image(
            painter = painterResource(R.drawable.f1_logo),
            contentDescription = stringResource(R.string.splash_logo_description),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .graphicsLayer {
                    alpha = progress.value
                    scaleX = 0.85f + 0.15f * progress.value
                    scaleY = 0.85f + 0.15f * progress.value
                }
        )
    }
}

/** Three slanted red bands that fade out at the top and bottom, like motion trails. */
@Composable
private fun SpeedStreaks(modifier: Modifier = Modifier) {
    val red = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier) {
        val skew = size.height * 0.45f

        fun streak(left: Float, width: Float, alpha: Float) {
            val path = Path().apply {
                moveTo(left + skew, 0f)
                lineTo(left + skew + width, 0f)
                lineTo(left + width, size.height)
                lineTo(left, size.height)
                close()
            }
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    listOf(
                        red.copy(alpha = 0f),
                        red.copy(alpha = alpha),
                        red.copy(alpha = 0f)
                    )
                )
            )
        }

        streak(left = size.width * 0.02f, width = size.width * 0.10f, alpha = 0.45f)
        streak(left = size.width * 0.20f, width = size.width * 0.03f, alpha = 0.30f)
        streak(left = size.width * 0.62f, width = size.width * 0.16f, alpha = 0.35f)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        SplashScreen(onFinished = {})
    }
}