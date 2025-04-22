package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository)  {
    suspend fun execute(userName:String, password:String, name:String) = userRepository.register(userName, password, name)
}