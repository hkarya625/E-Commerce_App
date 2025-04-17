package com.himanshu_kumar.domain.model.request

data class AddCartRequestModel(
    val productId: Int,
    val productName:String,
    val price:Int,
    val quantity:Int,
    val userId:Int
)