package br.com.breno.orgs.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val image: String? = null,
    val name: String,
    val description: String,
    val value: BigDecimal,
) : Parcelable

