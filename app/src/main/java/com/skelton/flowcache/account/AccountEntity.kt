package com.skelton.flowcache.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountEntity(
    val name: String = "",
    val email: String = "",
    val address: String = ""
)