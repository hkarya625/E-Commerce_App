package com.himanshu_kumar.data.model.response

import com.himanshu_kumar.data.model.DataCategory
import com.himanshu_kumar.domain.model.ProductListModel
import kotlinx.serialization.Serializable


@Serializable
data class ProductListResponse(
    val category: DataCategory,
    val creationAt: String,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val slug: String,
    val title: String,
    val updatedAt: String
){
    fun toProductList() = ProductListModel(
        category = category.id,
        description = description,
        id = id,
        images = images,
        price = price,
        title = title
    )
}