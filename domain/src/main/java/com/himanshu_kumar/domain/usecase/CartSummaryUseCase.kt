package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.CartRepository

class CartSummaryUseCase(private val cartRepository:CartRepository) {
    suspend fun execute(userId:Long) = cartRepository.getSummary(userId)
}