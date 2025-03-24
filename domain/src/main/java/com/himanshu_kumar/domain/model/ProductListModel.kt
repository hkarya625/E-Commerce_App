package com.himanshu_kumar.domain.model

data class ProductListModel(
    val category: Int,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String,
){
    val priceString:String
    get() = "$price"
}