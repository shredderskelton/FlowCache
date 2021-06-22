package com.skelton.flowcache

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        DefaultMainViewModel(
            InMemoryAccountRepository(
                DataCacheMemory(
                    TimeProviderImpl()
                ),
                DefaultAccountDataSource(
                    DefaultFirestoreCollectionProvider()
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.refreshButton).setOnClickListener {
            viewModel.refresh()
        }
        viewModel.bind()
    }

    private fun MainViewModel.bind() {
        bind(name) {
            findViewById<TextView>(R.id.nameEditText).text = it
        }
        bind(address) {
            findViewById<TextView>(R.id.addressEditText).text = it
        }
        bind(email) {
            findViewById<TextView>(R.id.emailEditText).text = it
        }
        bind(cacheNoteVisible) {
            findViewById<View>(R.id.cacheWarning).isVisible = it
        }
        bind(errorVisible) {
            findViewById<View>(R.id.errorWarning).isVisible = it
        }
        bind(errorText) {
            findViewById<TextView>(R.id.errorWarning).text = it
        }
    }
}

fun <T> AppCompatActivity.bind(to: Flow<T>, observer: (T) -> Unit) {
    to.onEach { observer(it) }
        .launchIn(lifecycleScope)
}
