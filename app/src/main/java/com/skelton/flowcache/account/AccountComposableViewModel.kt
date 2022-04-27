package com.skelton.flowcache.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.DataResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

abstract class AccountComposableViewModel : ViewModel() {
    abstract val state: Flow<AccountState>
    abstract fun refresh(force: Boolean = false)
    abstract fun reset()
}

class DefaultComposableAccountViewModel(
    private val repository: AccountRepository,
    private val config: AppConfig
) : AccountComposableViewModel() {

    private val refreshTrigger = MutableStateFlow(RefreshParameters())

    override val state: Flow<AccountState> =
        refreshTrigger
            .flatMapLatest { parameters ->
                repository.getAccount(
                    name = parameters.accountName,
                    force = parameters.force
                ).transformLatest {
                    when (it) {
                        is DataResult.Error -> emit(AccountState.Error(it.errorMessage))
                        is DataResult.Success.Cache -> {
                            val state =
                                AccountState.Success(it.data, isCachedNoteVisible = false)
                            emit(state)
                            delay(config.maximumDataAge.toMillis()) // This will be interrupted if the repository emits a new value
                            emit(state.copy(isCachedNoteVisible = true))
                        }
                        is DataResult.Success.Network ->
                            emit(AccountState.Success(it.data, isCachedNoteVisible = false))
                    }
                }.onStart { emit(AccountState.Loading) }
            }

    override fun refresh(force: Boolean) {
        refreshTrigger.value = RefreshParameters(force = force)
    }

    override fun reset() {
        viewModelScope.launch {
            repository.createAccount(
                name = "nick",
                details = AccountDetails(
                    name = "Nick Skelton",
                    email = "nick.g.skelton@gmail.com",
                    address = "22 Grünwaldstr. \n81436 München"
                )
            )
            refresh(true)
        }
    }
}

class RefreshParameters(val force: Boolean = false, val accountName: String = "nick")

sealed class AccountState {
    object Loading : AccountState()
    data class Error(val text: String) : AccountState()
    data class Success(
        val data: AccountDetails,
        val isCachedNoteVisible: Boolean = false,
    ) : AccountState()
}