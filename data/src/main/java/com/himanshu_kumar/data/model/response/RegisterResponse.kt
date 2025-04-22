package com.himanshu_kumar.data.model.response

import com.himanshu_kumar.domain.model.UserDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val email: String,
    val password: String,
    val name: String,
    val avatar: String,
    val role:String,
    val id: Int,
){
    fun toUserDomainModel(): UserDomainModel {
        return UserDomainModel(
            id = id,
            email = email,
            password = password,
            name = name,
            role = role,
            avatar = avatar,
        )
    }
}