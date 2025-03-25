package com.himanshu_kumar.domain.model

data class ProductListModel(
    val category:ProductCategory,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String,
){
    val priceString:String
    get() = "$price"
}