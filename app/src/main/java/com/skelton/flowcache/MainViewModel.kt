package com.skelton.flowcache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlin.random.Random

abstract class MainViewModel : ViewModel() {
    abstract val name: Flow<String>
    abstract val email: Flow<String>
    abstract val address: Flow<String>
    abstract val cacheNoteVisible: Flow<Boolean>
    abstract val errorText: Flow<String>
    abstract val errorVisible: Flow<Boolean>
    abstract val loadingVisible: Flow<Boolean>
    abstract fun refresh(force: Boolean = false)
}

class DefaultMainViewModel(
    private val repository: AccountRepository,
    private val config: AppConfig
) : MainViewModel() {

    private val refreshTrigger = MutableStateFlow(RefreshParameters())

    private val accountResult = refreshTrigger.flatMapLatest {
        println("Getting Account")
        repository.getAccount(
            name = it.accountName,
            force = it.force
        )
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    override val name = accountResult.mapSuccess { it.name }
    override val email = accountResult.mapSuccess { it.email }
    override val address = accountResult.mapSuccess { it.address }
    override val loadingVisible = repository.isLoading

    private fun Flow<Result<AccountDetails>>.mapSuccess(block: (AccountDetails) -> String) = map {
        when (it) {
            is Result.Success -> block(it.data)
            is Result.Error -> "N/A"
        }
    }

    override val cacheNoteVisible = accountResult
        .map {
            when (it) {
                is Result.Success.Network -> false
                is Result.Success.Cache -> true
                is Result.Error -> false
            }
        }
        .mapLatest {
            if (it) delay(config.staleDataAge.toMillis())
            it
        }

    override val errorVisible = accountResult.map {
        when (it) {
            is Result.Success -> false
            is Result.Error -> true
        }
    }

    override val errorText = accountResult.map {
        when (it) {
            is Result.Success -> ""
            is Result.Error -> it.errorMessage
        }
    }

    override fun refresh(force: Boolean) {
        refreshTrigger.value = RefreshParameters()
    }
}

private data class RefreshParameters(
    val accountName:String = "nick",
    val force: Boolean = false,
    val trigger: Long = Random.nextLong(),
)