package com.skelton.flowcache.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skelton.flowcache.ui.theme.FlowCacheTheme

@Composable
fun AccountComposable(
    state: State<AccountState>,
    onRefresh: (Boolean) -> Unit,
    onReset: () -> Unit,
) {
    Column {
        CacheNotification(state = state.value)
        Column(modifier = Modifier.padding(all = 12.dp)) {
            Text(text = "Account Details", fontSize = 30.sp)
            when (val result = state.value) {
                is AccountState.Success -> Success(result, onRefresh)
                is AccountState.Error -> Error(result, onRefresh, onReset)
                AccountState.Loading -> Loading()
            }
        }
    }
}

@Composable
fun Success(result: AccountState.Success, onRefresh: (Boolean) -> Unit) {
    val textModifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()

    OutlinedTextField(
        modifier = textModifier,
        value = result.data.name,
        onValueChange = { /* TODO */ },
        label = { Text("Name") })
    OutlinedTextField(
        modifier = textModifier,
        value = result.data.email,
        onValueChange = { /* TODO */ },
        label = { Text("Email") })
    OutlinedTextField(
        modifier = textModifier,
        value = result.data.address,
        onValueChange = { /* TODO */ },
        label = { Text("Address") })
    RefreshRowComposable(onRefresh)
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(6.dp))
        Text(
            text = "Loading...",
            color = Blue,
        )
    }
}

@Composable
fun Error(result: AccountState.Error, onRefresh: (Boolean) -> Unit, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Rounded.Error,
            modifier = Modifier.size(80.dp),
            contentDescription = "",
        )
        Text(text = "Error: ${result.text}", color = Red)
        Button(
            onClick = { onReset() },
        ) { Text(text = "Reset") }
    }
    RefreshRowComposable(onRefresh)
}

@Composable
fun CacheNotification(state: AccountState) {
    AnimatedVisibility(
        visible = state is AccountState.Success && state.isCachedNoteVisible,
        enter = slideInVertically { height -> -height },
        exit = slideOutVertically { height -> -height }
    ) {
        Text(
            text = "The data you are viewing may be out of date. Are you having trouble connecting to the internet?",
            color = White,
            modifier = Modifier
                .background(Blue.copy(alpha = 0.5f))
                .padding(12.dp)
        )
    }
}

@Composable
fun RefreshRowComposable(onRefresh: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        val buttonMod = Modifier.weight(1F)
        Button(
            modifier = buttonMod.padding(end = 6.dp),
            onClick = { onRefresh(false) }
        ) { Text(text = "Refresh") }
        Button(
            modifier = buttonMod.padding(start = 6.dp),
            onClick = { onRefresh(true) }
        ) { Text(text = "Force Refresh") }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    FlowCacheTheme {
        AccountComposable(
            rememberUpdatedState(
                AccountState.Error("Something went wrong")
            ),
            {}, {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    FlowCacheTheme {
        AccountComposable(
            rememberUpdatedState(AccountState.Loading),
            {}, {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessPreview() {
    FlowCacheTheme {
        AccountComposable(
            rememberUpdatedState(
                AccountState.Success(
                    AccountDetails(
                        name = "Nick Skelton",
                        email = "nick.g.skelton@gmail.com",
                        address = "22 Acacia Ave",
                    ),
                    isCachedNoteVisible = true
                )
            ),
            {}, {},
        )
    }
}