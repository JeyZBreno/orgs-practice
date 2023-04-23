package br.com.breno.orgs.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.formatToBrazilCurrency(): String =
    NumberFormat
        .getCurrencyInstance(
            Locale("pt", "br")
        ).format(this)