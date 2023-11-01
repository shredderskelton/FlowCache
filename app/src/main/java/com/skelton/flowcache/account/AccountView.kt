package com.skelton.flowcache.account

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skelton.flowcache.account.composables.CacheNotificationView
import com.skelton.flowcache.account.composables.ErrorView
import com.skelton.flowcache.account.composables.LoadingView
import com.skelton.flowcache.account.composables.SuccessView
import com.skelton.flowcache.ui.theme.FlowCacheTheme

@Composable
fun AccountView(
    state: State<AccountViewState>,
    onRefresh: (Boolean) -> Unit,
    onReset: () -> Unit,
    onCreate: () -> Unit,
) {
    Column {
        CacheNotificationView(state = state.value)
        Column(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxHeight()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearEasing
                    )
                )
        ) {
            Text(text = "Account Details", fontSize = 30.sp)
            when (val result = state.value) {
                is AccountViewState.Success -> SuccessView(result, onRefresh, onReset)
                is AccountViewState.Error -> ErrorView(result, onRefresh, onReset)
                AccountViewState.Loading -> LoadingView()
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onCreate,
            ) {
                Text("Create User")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    FlowCacheTheme {
        AccountView(
            rememberUpdatedState(
                AccountViewState.Error("Something went wrong")
            ),
            {}, {}, {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    FlowCacheTheme {
        AccountView(
            rememberUpdatedState(AccountViewState.Loading),
            {}, {}, {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessPreview() {
    FlowCacheTheme {
        AccountView(
            rememberUpdatedState(
                AccountViewState.Success(
                    AccountData(
                        name = "Nick Skelton",
                        email = "nick.g.skelton@gmail.com",
                        address = "22 Acacia Ave",
                    ),
                    isCachedNoteVisible = true
                )
            ),
            {}, {}, {},
        )
    }
}