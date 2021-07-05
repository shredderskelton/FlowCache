package com.skelton.flowcache.account.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skelton.flowcache.account.AccountDataSource
import com.skelton.flowcache.account.AccountDetails
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
    private val dataSource: AccountDataSource,
) : CreateViewModel() {
    override fun create(id: String, name: String, email: String, address: String) {
        viewModelScope.launch {
            dataSource.createAccount(
                name = id,
                details = AccountDetails(name, email, address)
            )
        }
    }
}
