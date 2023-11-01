package com.skelton.flowcache.account.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skelton.flowcache.account.AccountViewState


@Composable
fun CacheNotificationView(state: AccountViewState) {
    AnimatedVisibility(
        visible = state is AccountViewState.Success && state.isCachedNoteVisible,
        enter = slideInVertically { height -> -height },
        exit = slideOutVertically { height -> -height }
    ) {
        Text(
            text = "The data you are viewing may be out of date. Are you having trouble connecting to the internet?",
            color = Color.White,
            modifier = Modifier
                .background(Color.Blue.copy(alpha = 0.5f))
                .padding(12.dp)
        )
    }
}
