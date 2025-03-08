package com.himanshu_kumar.data.model

import com.himanshu_kumar.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
class DataProductModel(                             // works as dto(data access object)
    val id:Long,
    val title:String,
    val price:Double,
    val category:String,
    val description:String,
    val image:String
) {
    fun toProduct() = Product(                      // converts DataProductModel to Product for use in domain layer
        id = id,
        title = title,
        price = price,
        category = category,
        description = description,
        image = image,
    )
}