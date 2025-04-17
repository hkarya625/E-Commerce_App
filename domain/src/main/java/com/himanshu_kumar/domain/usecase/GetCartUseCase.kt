package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.CartRepository

class GetCartUseCase(private val cartRepository: CartRepository) {
    suspend fun execute() = cartRepository.getCart()
}