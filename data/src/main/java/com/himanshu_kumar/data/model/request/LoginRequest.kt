package com.himanshu_kumar.data.model.request

import kotlinx.serialization.Serializable


@Serializable
data class TokenRequest(
    val email: String,
    val password:String
)

@Serializable
data class LoginRequest(
    val token:String
)
