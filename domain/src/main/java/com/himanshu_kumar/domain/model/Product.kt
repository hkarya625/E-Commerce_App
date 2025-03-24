package com.himanshu_kumar.domain.model

data class Product(
    val categoryId: Int,
    val id:Int,
    val title:String,
    val price:Double,
    val description:String,
    val image:String
){
    val priceString:String
        get() = "$price"
}


