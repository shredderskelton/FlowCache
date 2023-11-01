package com.skelton.flowcache.account

sealed class AccountViewState {
    object Loading : AccountViewState()
    data class Error(
        val text: String,
        val isBackgroundRefreshing: Boolean = false,
    ) : AccountViewState()

    data class Success(
        val data: AccountData,
        val isBackgroundRefreshing: Boolean = false,
        val isCachedNoteVisible: Boolean = false,
    ) : AccountViewState()
}