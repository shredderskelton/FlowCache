package com.skelton.flowcache.account

import androidx.lifecycle.viewModelScope
import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.DataResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

class AccountViewModelDefault(
    private val repository: AccountRepository,
    private val config: AppConfig
) : AccountViewModel() {

    private val refreshTrigger = MutableStateFlow(RefreshParameters())

    val _state: Flow<AccountViewState> =
        refreshTrigger
            .flatMapLatest { parameters ->
                repository.getAccount(
                    name = parameters.accountName,
                    force = parameters.force
                ).transformLatest {
                    when (it) {
                        is DataResult.Error -> emit(AccountViewState.Error(it.errorMessage))
                        is DataResult.Success.Cache -> {
                            val state =
                                AccountViewState.Success(it.data, isCachedNoteVisible = false)
                            emit(state)
                            delay(config.maximumDataAge.toMillis()) // This will be interrupted if the repository emits a new value
                            emit(state.copy(isCachedNoteVisible = true))
                        }

                        is DataResult.Success.Network ->
                            emit(AccountViewState.Success(it.data, isCachedNoteVisible = false))
                    }
                }.onStart { emit(AccountViewState.Loading) }
            }

    override fun refresh(force: Boolean) {
        refreshTrigger.value = RefreshParameters(force = force)
    }

    override val state: Flow<AccountViewState> =
        combine(_state, repository.isRefreshing) { s, refreshing ->
            when (s) {
                is AccountViewState.Error -> s.copy(isBackgroundRefreshing = refreshing)
                AccountViewState.Loading -> s
                is AccountViewState.Success -> s.copy(isBackgroundRefreshing = refreshing)
            }
        }

    override fun reset() {
        viewModelScope.launch {
            repository.createAccount(
                name = "nick",
                details = AccountData(
                    name = "Nick Skelton",
                    email = "nick.g.skelton@gmail.com",
                    address = "22 Grünwaldstr. \n81436 München"
                )
            )
            refresh(true)
        }
    }
}

private class RefreshParameters(val force: Boolean = false, val accountName: String = "nick")

