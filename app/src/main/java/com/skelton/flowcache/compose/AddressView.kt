package com.skelton.flowcache.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddressView(userId: String) {
    val viewModel: AddressViewModel = koinViewModel { parametersOf(userId) }
    val state = viewModel.state.collectAsStateWithLifecycle(AddressState.Loading)
    AddressViewInternal(state.value)
}

@Composable
fun AddressViewInternal(
    state: AddressState
) {
    TODO()
}

sealed class AddressState {
    data class Success(val text: String) : AddressState()
    data object Error : AddressState()
    data object Loading : AddressState()
}


class AddressViewModel(
    private val userId: String,
) : ViewModel() {
    val state: Flow<AddressState> = TODO()
}