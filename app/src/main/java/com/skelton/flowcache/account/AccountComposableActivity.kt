package com.skelton.flowcache.account

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.cache.DataCacheMemory
import com.skelton.flowcache.system.DefaultFirestoreCollectionProvider
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
                dataSource = DefaultAccountDataSource(
                    DefaultFirestoreCollectionProvider(),
                    config
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
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.animateContentSize()) {
                        AccountComposable(
                            state = viewModel.state.collectAsState(
                                initial = AccountState.Error("")
                            ),
                            onRefresh = { viewModel.refresh(it) },
                            onReset = { viewModel.reset() }
                        )
                    }
                }
            }
        }
    }

}