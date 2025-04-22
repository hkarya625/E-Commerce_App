package com.himanshu_kumar.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val avatar:String = ""
)
