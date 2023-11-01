package com.skelton.flowcache.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class AccountViewModel : ViewModel() {
    abstract val state: Flow<AccountViewState>
    abstract fun refresh(force: Boolean = false)
    abstract fun reset()
}
