package com.himanshu_kumar.data.model.request

import com.himanshu_kumar.domain.model.AddressDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class AddressOrderModel(
    val addressLine:String,
    val city:String,
    val state:String,
    val postalCode:String,
    val country:String
){
    companion object{
        fun fromDomainAddress(userAddress: AddressDomainModel) =
            AddressOrderModel(
                addressLine = userAddress.addressLine,
                city = userAddress.city,
                state = userAddress.state,
                postalCode = userAddress.postalCode,
                country = userAddress.country
            )
    }
}