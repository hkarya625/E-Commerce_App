package com.himanshu_kumar.data.model

import com.himanshu_kumar.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
class DataProductModel(                             // works as dto(data access object)
    val category: DataCategory,
    val id:Int,
    val title:String,
    val price:Double,
    val description:String,
    val image:String
) {
    fun toProduct() = Product(
        categoryId = category.id,
        id = id,
        title = title,
        price = price,
        description = description,
        image = image                      // converts DataProductModel to Product for use in domain layer
    )
}