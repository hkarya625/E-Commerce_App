package com.himanshu_kumar.shoppingapp.navigation

import android.os.Parcelable
import com.himanshu_kumar.shoppingapp.model.UserAddress
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class UserAddressWrapper(
    val userAddress: UserAddress?
):Parcelable