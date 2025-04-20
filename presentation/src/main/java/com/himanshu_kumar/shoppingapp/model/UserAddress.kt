package com.himanshu_kumar.shoppingapp.model

import android.os.Parcelable
import com.himanshu_kumar.domain.model.AddressDomainModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable                        // used for converting format that can be stored and transmitted
@Parcelize                           // used for passing data between android components
data class UserAddress(
    val addressLine:String,
    val city:String,
    val state:String,
    val postalCode:String,
    val country:String
):Parcelable{
    override fun toString(): String {
        return "$addressLine, $city, $state, $postalCode, $country"
    }
    fun toDomainAddress() = AddressDomainModel(
        addressLine = addressLine,
        city = city,
        state = state,
        postalCode = postalCode,
        country = country
    )
}