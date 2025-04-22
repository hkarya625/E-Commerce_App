package com.himanshu_kumar.data.repository

import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.CartSummary
import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import com.himanshu_kumar.domain.network.NetworkService
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.repository.CartRepository

class CartRepositoryImpl(private val networkService: NetworkService) : CartRepository {
    override suspend fun addProductToCart(request: AddCartRequestModel, userId: Long): ResultWrapper<CartModel> {
        return networkService.addProductToCart(request, userId)
    }

    override suspend fun getCart(userId:Long): ResultWrapper<CartModel> {
        return networkService.getCart(userId)
    }

    override suspend fun updateQuantity(cartItemModel: CartItemModel, userId: Long): ResultWrapper<CartModel> {
        return networkService.updateQuantity(cartItemModel, userId)
    }

    override suspend fun deleteItem(cartItemId: Int, userId:Long): ResultWrapper<CartModel> {
        return networkService.deleteItem(cartItemId, userId)
    }

    override suspend fun getSummary(userId: Long): ResultWrapper<CartSummary> {
        return networkService.getCartSummary(userId)
    }
}