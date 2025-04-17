package com.himanshu_kumar.data.model.request

import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartRequest(
    val productId: Int,
    val productName: String,
    val price: Int,
    val quantity: Int,
    val userId: Int
){
    companion object{
        fun fromCartRequestModel(addToCartRequest: AddCartRequestModel) = AddToCartRequest(
            productId = addToCartRequest.productId,
            productName = addToCartRequest.productName,
            price = addToCartRequest.price,
            quantity = addToCartRequest.quantity,
            userId = addToCartRequest.userId
        )
    }
}