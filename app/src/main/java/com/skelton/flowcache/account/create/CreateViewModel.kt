package com.skelton.flowcache.account.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skelton.flowcache.account.AccountApi
import com.skelton.flowcache.account.AccountEntity
import kotlinx.coroutines.launch

abstract class CreateViewModel : ViewModel() {
    abstract fun create(
        id: String,
        name: String,
        email: String,
        address: String,
    )
}

class DefaultCreateViewModel(
    private val dataSource: AccountApi,
) : CreateViewModel() {
    override fun create(id: String, name: String, email: String, address: String) {
        viewModelScope.launch {
            dataSource.createAccount(
                id = id,
                details = AccountEntity(name, email, address)
            )
        }
    }
}
