package br.com.breno.orgs.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.R
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductDetailBinding
import br.com.breno.orgs.extensions.formatToBrazilCurrency
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.utils.KEY_PRODUCT

class ProductDetailActivity: AppCompatActivity() {

    private lateinit var product: Product
    private val binding by lazy { ActivityProductDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tryLoadProduct()
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(::product.isInitialized) {
            val db = OrgsDatabase.getInstance(this)
            val productDao = db.productDao()
            when(item.itemId) {
                R.id.menu_product_detail_edit -> {
                    Intent(this, ProductFormActivity::class.java).apply {
                        putExtra(KEY_PRODUCT, product)
                        startActivity(this)
                    }
                }
                R.id.menu_product_detail_delete -> {
                    productDao.deleteItem(product)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryLoadProduct() {
        intent.getParcelableExtra<Product>(KEY_PRODUCT)?.let { loadedProduct ->
            product = loadedProduct
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