package com.himanshu_kumar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DataCategory(
    val creationAt: String,
    val id: Int,
    val image: String,
    val name: String,
    val slug: String,
    val updatedAt: String
)