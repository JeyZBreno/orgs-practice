package br.com.breno.orgs.model

import android.os.Parcelable
import java.math.BigDecimal
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val image: String? = null,
    val name: String,
    val description: String,
    val value: BigDecimal,
) : Parcelable

