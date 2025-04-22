package com.himanshu_kumar.data.model.response

import com.himanshu_kumar.domain.model.UserDomainModel
import kotlinx.serialization.Serializable


@Serializable
data class TokenResponse(
    val access_token:String,
    val refresh_token:String
)

@Serializable
data class UserResponse(
    val id:Int,
    val email:String,
    val password:String,
    val name:String,
    val role:String,
    val avatar:String
) {
    fun toUserDomainModel():UserDomainModel{
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


