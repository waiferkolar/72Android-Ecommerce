package coder.seventy.two.ecommerce.modals

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CartProduct(
    val id: Int,
    val cat_id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val description: String,
    val count: Int
) : Parcelable