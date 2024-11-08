package com.skelton.flowcache.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow


@Composable
fun ProfileScreenOld(
    state: ProfileState
) {
    NameViewInternal(state.name)
    AddressViewInternal(state.address)
}


@Composable
fun ProfileView(userId: String) {
    AddressView(userId)
    NameView(userId)
}

data class ProfileState(
    val name: NameState,
    val address: AddressState,
)

class ProfileViewModel(
    private val userId: String,
) : ViewModel() {
    val state: Flow<ProfileState> = TODO()
}