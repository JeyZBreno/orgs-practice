package br.com.breno.orgs.model

import java.math.BigDecimal

data class Product(
    val image: String? = null,
    val name: String,
    val description: String,
    val value: BigDecimal,
)

