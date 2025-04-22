package com.himanshu_kumar.domain.network

import com.himanshu_kumar.domain.model.AddressDomainModel
import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.model.CartModel
import com.himanshu_kumar.domain.model.CartSummary
import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.model.OrdersListModel

import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.domain.model.request.AddCartRequestModel

interface NetworkService{
    suspend fun getProducts(category:Int?):ResultWrapper<List<ProductListModel>>
    suspend fun getCategories():ResultWrapper<List<CategoriesListModel>>
    suspend fun addProductToCart(request: AddCartRequestModel, userId:Long):ResultWrapper<CartModel>
    suspend fun getCart(userId:Long):ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel,userId:Long):ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId: Int, userId: Long):ResultWrapper<CartModel>
    suspend fun getCartSummary(userId:Long):ResultWrapper<CartSummary>
    suspend fun placeOrder(address: AddressDomainModel, userId:Long):ResultWrapper<Long>
    suspend fun getOrderList(userId:Long):ResultWrapper<OrdersListModel>
    suspend fun login(email:String, password:String):ResultWrapper<UserDomainModel>
    suspend fun register(email: String, password: String, name:String):ResultWrapper<UserDomainModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value:T):ResultWrapper<T>()
    data class Failure(val message:String):ResultWrapper<Nothing>()
}
