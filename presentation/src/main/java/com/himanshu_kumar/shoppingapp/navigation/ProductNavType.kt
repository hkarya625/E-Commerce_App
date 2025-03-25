package com.himanshu_kumar.shoppingapp.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64

val productNavType = object:NavType<UiProductModel>(isNullableAllowed = false){
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(bundle: Bundle, key: String): UiProductModel? {
        return bundle.getParcelable(key, UiProductModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun parseValue(value: String): UiProductModel {
        val item = Json.decodeFromString<UiProductModel>(value)
        return item.copy(
            images = item.images.map {
                URLDecoder.decode(it, "UTF-8")
            },
            description = String(Base64.getDecoder().decode(item.description.replace("-", "/"))),
            title = String(Base64.getDecoder().decode(item.title.replace("/", "-")))
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serializeAsValue(value: UiProductModel): String {
        val encoded = value.copy(
            images = value.images.map {
                URLEncoder.encode(it, "UTF-8")
            },
            description = String(Base64.getEncoder().encode(value.description.toByteArray())).replace("/", "-"),
            title = String(Base64.getEncoder().encode(value.title.toByteArray())).replace("/", "-")
        )
        return Json.encodeToString(encoded)
    }

    override fun put(bundle: Bundle, key: String, value: UiProductModel) {
        bundle.putParcelable(key, value)
    }

}