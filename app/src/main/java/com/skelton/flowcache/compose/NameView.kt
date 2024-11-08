package com.skelton.flowcache.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NameView(userId: String) {
    val viewModel: NameViewModel = koinViewModel { parametersOf(userId) }
    val state = viewModel.state.collectAsStateWithLifecycle(NameState.Loading)
    NameViewInternal(state.value)
}

@Composable
fun NameViewInternal(
    state: NameState
) {
    Text(
        when (state) {
            NameState.Error -> "Error"
            NameState.Loading -> "Loading"
            is NameState.Success -> state.text
        }
    )
}

@Preview
@Composable
fun NamePreviewLoading(){
    NameViewInternal(NameState.Loading)
}

@Preview
@Composable
fun NamePreviewError(){
    NameViewInternal(NameState.Error)
}

@Preview
@Composable
fun NamePreviewSuccess(){
    NameViewInternal(NameState.Success("Nick"))
}

sealed class NameState {
    data class Success(val text: String) : NameState()
    data object Error : NameState()
    data object Loading : NameState()
}


class NameViewModel(
    private val userId: String,
) : ViewModel() {
    val state: Flow<NameState> = TODO()
}