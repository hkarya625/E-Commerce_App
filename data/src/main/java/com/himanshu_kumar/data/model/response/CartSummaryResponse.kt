package com.himanshu_kumar.data.model.response

import com.himanshu_kumar.domain.model.CartSummary
import kotlinx.serialization.Serializable

@Serializable
data class CartSummaryResponse(
    val data : Summary,
    val msg:String
){
    fun toCartSummary() = CartSummary(
        data = data.toSummaryData(),
        msg = msg
    )
}