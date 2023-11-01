package com.skelton.flowcache.account.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skelton.flowcache.account.AccountViewState


@Composable
fun ErrorView(
    result: AccountViewState.Error,
    onRefresh: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            modifier = Modifier.size(80.dp),
            contentDescription = "",
        )
        Text(text = "Error: ${result.text}", color = Color.Red)
        Button(
            onClick = { onReset() },
        ) { Text(text = "Reset") }
    }
    RefreshRowView(result.isBackgroundRefreshing, onRefresh, onReset)
}