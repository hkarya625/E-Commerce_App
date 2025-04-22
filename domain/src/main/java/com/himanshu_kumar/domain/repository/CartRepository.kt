package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.CartSummary
import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import com.himanshu_kumar.domain.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(request: AddCartRequestModel, userId:Long): ResultWrapper<CartModel>
    suspend fun getCart(userId:Long): ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel,userId:Long): ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId: Int, userId:Long): ResultWrapper<CartModel>
    suspend fun getSummary(userId: Long): ResultWrapper<CartSummary>
}