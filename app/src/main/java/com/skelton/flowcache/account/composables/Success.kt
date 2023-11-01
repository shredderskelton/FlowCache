package com.skelton.flowcache.account.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skelton.flowcache.account.AccountData
import com.skelton.flowcache.account.AccountView
import com.skelton.flowcache.account.AccountViewState
import com.skelton.flowcache.ui.theme.FlowCacheTheme


@Composable
fun SuccessView(
    result: AccountViewState.Success,
    onRefresh: (Boolean) -> Unit,
    onReset: () -> Unit,
) {
    val textModifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()

    OutlinedTextField(
        modifier = textModifier,
        value = result.data.name,
        onValueChange = { },
        label = { Text("Name") })
    OutlinedTextField(
        modifier = textModifier,
        value = result.data.email,
        onValueChange = { },
        label = { Text("Email") })
    OutlinedTextField(
        modifier = textModifier,
        value = result.data.address,
        onValueChange = { },
        label = { Text("Address") })
    RefreshRowView(result.isBackgroundRefreshing, onRefresh, onReset)
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
                    isCachedNoteVisible = true,
                    isBackgroundRefreshing = true,
                )
            ),
            {}, {}, {},
        )
    }
}