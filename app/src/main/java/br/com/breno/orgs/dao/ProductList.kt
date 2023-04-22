package br.com.breno.orgs.dao

import br.com.breno.orgs.model.Product
import java.math.BigDecimal

class ProductList {

    fun addProducts(product: Product) {
        products.add(product)
    }

    fun findAll() : List<Product> {
        return products.toList()
    }

    companion object {
        private val products = mutableListOf<Product>(
            Product(
                name = "Salada de Frutas",
                description = "Laranja, Uva e Maça",
                value = BigDecimal("15.0")
            )
        )
    }
}