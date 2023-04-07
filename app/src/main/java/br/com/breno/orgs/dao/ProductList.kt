package br.com.breno.orgs.dao

import br.com.breno.orgs.model.Product

class ProductList {

    fun addProducts(product: Product) {
        products.add(product)
    }

    fun findAll() : List<Product> {
        return products.toList()
    }

    companion object {
        private val products = mutableListOf<Product>()
    }
}