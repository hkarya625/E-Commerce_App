package com.himanshu_kumar.domain.network

import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.CategoriesListModel

import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.model.request.AddCartRequestModel

interface NetworkService{
    suspend fun getProducts(category:Int?):ResultWrapper<List<ProductListModel>>
    suspend fun getCategories():ResultWrapper<List<CategoriesListModel>>
    suspend fun addProductToCart(request: AddCartRequestModel):ResultWrapper<CartModel>
    suspend fun getCart():ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel):ResultWrapper<CartModel>
    suspend fun deleteItem(userId:Int, cartItemId:Int):ResultWrapper<CartModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value:T):ResultWrapper<T>()
    data class Failure(val message:String):ResultWrapper<Nothing>()
}
