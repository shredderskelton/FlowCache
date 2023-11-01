package com.skelton.flowcache.account.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skelton.flowcache.ui.theme.FlowCacheTheme

@Composable
fun RefreshRowView(
    isBackgroundRefreshing:Boolean,
    onRefresh: (Boolean) -> Unit,
    onReset: () -> Unit,
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            val buttonMod = Modifier.weight(1F)
            Button(
                modifier = buttonMod.padding(end = 6.dp),
                onClick = { onRefresh(false) }
            ) {
                if(isBackgroundRefreshing) LoadingIndicator() else
                Text(text = "Refresh")
            }
            Button(
                modifier = buttonMod.padding(start = 6.dp),
                onClick = { onRefresh(true) }
            ) { Text(text = "Force Refresh") }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            val buttonMod = Modifier.weight(1F)
            Button(
                modifier = buttonMod.padding(end = 6.dp),
                onClick = { onReset() }
            ) { Text(text = "Reset") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    FlowCacheTheme {
        RefreshRowView(true, onRefresh = {}, onReset = {})
    }
}
