package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import com.himanshu_kumar.domain.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>
    suspend fun getCart(): ResultWrapper<CartModel>
}