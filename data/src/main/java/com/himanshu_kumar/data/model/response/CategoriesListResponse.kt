package com.himanshu_kumar.data.model.response

import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable              // convert json object to kotlin and vice versa
data class CategoriesListResponse(
    val creationAt: String,
    val id: Int,
    val image: String,
    val name: String,
    val slug: String,
    val updatedAt: String
)
{
    fun toCategoryListModel() = CategoriesListModel(
        creationAt = creationAt,
        id = id,
        image = image,
        name = name,
        slug = slug,
        updatedAt = updatedAt
    )
}
