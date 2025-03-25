package com.himanshu_kumar.shoppingapp.navigation

import com.himanshu_kumar.shoppingapp.model.UiProductModel
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object CartScreen

@Serializable
object ProfileScreen

@Serializable
data class ProductDetails(val product:UiProductModel)