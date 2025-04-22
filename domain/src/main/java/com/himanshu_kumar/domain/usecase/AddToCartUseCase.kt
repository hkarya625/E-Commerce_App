package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.repository.CartRepository

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(request: AddCartRequestModel, userId:Long): ResultWrapper<CartModel> = cartRepository.addProductToCart(request, userId)
}