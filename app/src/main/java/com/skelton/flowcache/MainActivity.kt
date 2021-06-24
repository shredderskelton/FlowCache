package com.skelton.flowcache

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.skelton.flowcache.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        val config = AppConfig()
        DefaultMainViewModel(
            InMemoryAccountRepository(
                DataCacheMemory(
                    TimeProviderImpl()
                ),
                DefaultAccountDataSource(
                    DefaultFirestoreCollectionProvider(),
                    config
                )
            ),
            config
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.refreshButton.setOnClickListener {
            viewModel.refresh()
        }
        binding.createButton.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
        binding.swipeToRefreshView.setOnRefreshListener {
            viewModel.refresh(true)
        }
        viewModel.bind()
    }

    private fun MainViewModel.bind() {
        bind(name) {
            binding.nameEditText.setText(it)
        }
        bind(address) {
            binding.addressEditText.setText(it)
        }
        bind(email) {
            binding.emailEditText.setText(it)
        }
        bind(cacheNoteVisible) {
            binding.cacheWarning.isVisible = it
        }
        bind(errorVisible) {
            binding.errorWarning.isVisible = it
        }
        bind(errorText) {
            binding.errorWarning.text = it
        }
        bind(loadingVisible) {
            binding.progressBar.isVisible = it
            if (!it) binding.swipeToRefreshView.isRefreshing = false
        }
    }
}

fun <T> AppCompatActivity.bind(to: Flow<T>, observer: (T) -> Unit) {
    to.onEach { observer(it) }
        .launchIn(lifecycleScope)
}
