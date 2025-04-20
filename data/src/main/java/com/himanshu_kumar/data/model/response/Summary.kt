package com.himanshu_kumar.data.model.response

import com.himanshu_kumar.domain.model.SummaryData
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    val discount:Double,
    val items:List<CartItem>,
    val shipping:Double,
    val subtotal:Double,
    val tax:Double,
    val total:Double
){
    fun toSummaryData() = SummaryData(
        discount = discount,
        items = items.map { it.toCartItemModel() },
        shipping = shipping,
        subtotal = subtotal,
        tax = tax,
        total = total,
    )
}
