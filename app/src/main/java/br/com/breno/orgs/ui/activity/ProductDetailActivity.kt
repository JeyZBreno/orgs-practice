package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.databinding.ActivityProductDetailBinding
import br.com.breno.orgs.extensions.formatToBrazilCurrency
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.utils.KEY_PRODUCT

class ProductDetailActivity: AppCompatActivity() {

    private val binding by lazy { ActivityProductDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tryLoadProduct()
        setContentView(binding.root)
    }

    private fun tryLoadProduct() {
        intent.getParcelableExtra<Product>(KEY_PRODUCT)?.let { loadedProduct ->
            fillFields(loadedProduct)
        } ?: finish()

    }

    private fun fillFields( loadedProduct: Product) {
        with(binding) {
            contentImage.tryLoadImage(loadedProduct.image)
            detailName.text = loadedProduct.name
            detailDescricao.text = loadedProduct.description
            detailValue.text = loadedProduct.value.formatToBrazilCurrency()
        }
    }

}