package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.domain.network.ResultWrapper

interface UserRepository {
    suspend fun login(email:String, password:String):ResultWrapper<UserDomainModel>
    suspend fun register(email: String, password: String, name:String):ResultWrapper<UserDomainModel>
}