package br.com.breno.orgs.ui.activity

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

class ProductDetailActivity : AppCompatActivity() {

    private var productId: Long? = null
    private var product: Product? = null
    private val binding by lazy { ActivityProductDetailBinding.inflate(layoutInflater) }
    private val productDao by lazy {
        OrgsDatabase
            .getInstance(this)
            .productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tryLoadProduct()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        productId?.let { id ->
            product = productDao.findById(id)
        }
        product?.let { product ->
            fillFields(product)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_product_detail_edit -> {
                Intent(this, ProductFormActivity::class.java).apply {
                    putExtra(KEY_PRODUCT, product)
                    startActivity(this)
                }
            }

            R.id.menu_product_detail_delete -> {
                product?.let { product ->
                    productDao.deleteItem(product)
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryLoadProduct() {
        intent.getParcelableExtra<Product>(KEY_PRODUCT)?.let { loadedProduct ->
            productId = loadedProduct.id
        } ?: finish()
    }

    private fun fillFields(loadedProduct: Product) {
        with(binding) {
            contentImage.tryLoadImage(loadedProduct.image)
            detailName.text = loadedProduct.name
            detailDescricao.text = loadedProduct.description
            detailValue.text = loadedProduct.value.formatToBrazilCurrency()
        }
    }
}