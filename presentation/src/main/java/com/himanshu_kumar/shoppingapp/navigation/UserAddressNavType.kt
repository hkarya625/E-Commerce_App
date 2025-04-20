package com.himanshu_kumar.shoppingapp.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


val userAddressNavType = object: NavType<UserAddressWrapper>(isNullableAllowed = false){
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(bundle: Bundle, key: String): UserAddressWrapper? {               // Retrieving Data from the Bundle
        return bundle.getParcelable(key, UserAddressWrapper::class.java)            // decode object from bundle
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun parseValue(value: String): UserAddressWrapper {                   // Parsing JSON to UiProductModel
        val item = Json.decodeFromString<UserAddressWrapper>(value)
        return item
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serializeAsValue(value: UserAddressWrapper): String {                 // Converting object to json string
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: UserAddressWrapper) {            // Storing Data in the Bundle
        bundle.putParcelable(key, value)
    }

}