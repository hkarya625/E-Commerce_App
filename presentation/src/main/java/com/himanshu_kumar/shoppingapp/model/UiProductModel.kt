package com.himanshu_kumar.shoppingapp.model


import android.os.Parcelable
import com.himanshu_kumar.domain.model.ProductListModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class UiProductModel(
    val categoryId: Int,
    val id:Int,
    val title:String,
    val price:Int,
    val description:String,
    val images:List<String>
):Parcelable{                                                  // parcelable is used to pass data between screens
    companion object {
        fun fromProduct(product: ProductListModel) = UiProductModel(
            categoryId = product.category.id,
            id = product.id,
            title = product.title,
            price = product.price,
            description = product.description,
            images = product.images
        )
    }
}
