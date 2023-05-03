package br.com.breno.orgs.utils

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun fromDouble(value: Double?) : BigDecimal =
        value?.let { BigDecimal(value.toString()) } ?: BigDecimal.ZERO


    @TypeConverter
    fun bigDecimalToDouble(value: BigDecimal?) : Double? =
        value?.let { value.toDouble() }
}