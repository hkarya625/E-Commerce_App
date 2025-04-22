package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userName:String, password:String) = userRepository.login(userName, password)
}