package com.skelton.flowcache.account.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skelton.flowcache.ui.theme.FlowCacheTheme


private const val NumIndicators = 3
private const val IndicatorSize = 6
private const val AnimationDurationMillis = 300

//1
private const val AnimationDelayMillis = AnimationDurationMillis / NumIndicators

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    indicatorSpacing: Dp = 4.dp,
) {
    // 1
    val animatedValues = List(NumIndicators) { index ->
        var animatedValue by remember(key1 = Unit) { mutableStateOf(0f) }
        LaunchedEffect(key1 = Unit) {
            animate(
                initialValue = IndicatorSize / 2f,
                targetValue = -IndicatorSize / 2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = AnimationDurationMillis),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(AnimationDelayMillis * index),
                ),
            ) { value, _ -> animatedValue = value }
        }
        animatedValue
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        // 2
        animatedValues.forEach { animatedValue ->
            LoadingDot(
                modifier = Modifier
                    .padding(horizontal = indicatorSpacing)
                    .width(IndicatorSize.dp)
                    .aspectRatio(1f)
                    // 3
                    .offset(y = animatedValue.dp),
                color = color,
            )
        }
    }
}

@Composable
private fun LoadingDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color)
    )
}

@Preview
@Composable
fun load() {
    FlowCacheTheme {
        var isLoading by remember(key1 = Unit) { mutableStateOf(true) }
        Button(onClick = {
            isLoading = !isLoading
        }) {
            if (isLoading)
                LoadingIndicator()
            else
                Text(text = "Load")
        }
    }
}