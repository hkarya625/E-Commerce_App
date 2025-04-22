package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.repository.CartRepository

class DeleteProductUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(cartItemId:Int, userId:Long) = cartRepository.deleteItem(cartItemId, userId)
}