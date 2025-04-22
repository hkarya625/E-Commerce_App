package com.himanshu_kumar.data.repository

import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.domain.network.NetworkService
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.repository.UserRepository

class UserRepositoryImpl(private val networkService: NetworkService) : UserRepository  {
    override suspend fun login(email: String, password: String): ResultWrapper<UserDomainModel> {
        return networkService.login(email, password)
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserDomainModel> = networkService.register(email, password, name)
}