package com.skelton.flowcache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn

abstract class MainViewModel : ViewModel() {
    abstract val name: Flow<String>
    abstract val email: Flow<String>
    abstract val address: Flow<String>
    abstract val cacheNoteVisible: Flow<Boolean>
    abstract val errorText: Flow<String>
    abstract val errorVisible: Flow<Boolean>
    abstract fun refresh()
}

class DefaultMainViewModel(private val repository: AccountRepository) : MainViewModel() {

    private val accountResult = repository.accountDetails
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    override val name = accountResult.mapSuccess { it.name }
    override val email = accountResult.mapSuccess { it.email }
    override val address = accountResult.mapSuccess { it.address }

    private fun Flow<Result<AccountDetails>>.mapSuccess(block: (AccountDetails) -> String) = map {
        when (it) {
            is Result.Success -> block(it.data)
            is Result.Error -> "Error"
        }
    }

    override val cacheNoteVisible = accountResult
        .map {
            when (it) {
                is Result.Success.Network -> false
                is Result.Success.Cached -> true
                is Result.Error -> false
            }
        }
        .mapLatest {
            if (it) delay(5000)
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

    override fun refresh() {
        repository.refresh()
    }
}