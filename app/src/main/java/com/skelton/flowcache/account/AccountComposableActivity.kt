package com.skelton.flowcache.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.account.create.CreateActivity
import com.skelton.flowcache.cache.DataCacheMemory
import com.skelton.flowcache.system.TimeProviderImpl
import com.skelton.flowcache.ui.theme.FlowCacheTheme

class AccountComposableActivity : ComponentActivity() {

    private val viewModel: AccountComposableViewModel by lazy {
        val config = AppConfig()
        DefaultComposableAccountViewModel(
            repository = InMemoryAccountRepository(
                // cache =DataCacheNoOp,
                cache = DataCacheMemory(
                    TimeProviderImpl()
                ),
                dataSource = AccountDataSourceRest(
                    HttpProviderDefault,
                )
            ),
            config = config
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowCacheTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.animateContentSize()) {
                        AccountComposable(
                            state = viewModel.state.collectAsState(
                                initial = AccountState.Error("")
                            ),
                            onRefresh = { viewModel.refresh(it) },
                            onReset = { viewModel.reset() }
                        )
                        Button(onClick = {
                            val myIntent = Intent(this@AccountComposableActivity, CreateActivity::class.java)
                            startActivity(myIntent)
                        }) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }

}