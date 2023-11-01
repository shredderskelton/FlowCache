package com.skelton.flowcache

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.skelton.flowcache.account.AccountDataSourceRest
import com.skelton.flowcache.account.AccountRepositoryInMemory
import com.skelton.flowcache.account.AccountView
import com.skelton.flowcache.account.AccountViewModel
import com.skelton.flowcache.account.AccountViewModelDefault
import com.skelton.flowcache.account.AccountViewState
import com.skelton.flowcache.account.create.CreateActivity
import com.skelton.flowcache.cache.DataCacheMemory
import com.skelton.flowcache.system.HttpProviderDefault
import com.skelton.flowcache.system.TimeProviderImpl
import com.skelton.flowcache.ui.theme.FlowCacheTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AccountViewModel by lazy {
        val config = AppConfig()
        AccountViewModelDefault(
            repository = AccountRepositoryInMemory(
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
                        AccountView(
                            state = viewModel.state.collectAsState(
                                initial = AccountViewState.Error("")
                            ),
                            onRefresh = { viewModel.refresh(it) },
                            onReset = { viewModel.reset() },
                            onCreate = {

                                val myIntent = Intent(
                                    this@MainActivity,
                                    CreateActivity::class.java
                                )
                                startActivity(myIntent)

                            }
                        )

                    }
                }
            }
        }
    }

}