package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityAllProductsBinding
import br.com.breno.orgs.extensions.goTo
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.recyclerview.adapter.HeaderAdapter
import br.com.breno.orgs.ui.recyclerview.adapter.ProductListAdapter
import br.com.breno.orgs.utils.PRODUCT_KEY_ID
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AllProductsActivity : BaseUserActivity() {

    private val binding by lazy { ActivityAllProductsBinding.inflate(layoutInflater) }
    private val productDao by lazy { OrgsDatabase.getInstance(this).productDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.activityAllProductsRecyclerview
        lifecycleScope.launch {
            productDao.findAll().map { products ->
                products.sortedBy {
                    it.userId
                }.groupBy {
                    it.userId
                }.map { productsUser ->
                    createAllProductsWithHeaderAdapter(productsUser)
                }.flatten()
            }.collect { adapter ->
                recyclerView.adapter = ConcatAdapter(adapter)
            }
        }
    }

    private fun createAllProductsWithHeaderAdapter(productsUser: Map.Entry<String?, List<Product>>) =
        listOf(
            HeaderAdapter(this, listOf(productsUser.key)),
            ProductListAdapter(this, productsUser.value) { clickedProduct ->
                goTo(ProductDetailActivity::class.java) {
                    putExtra(PRODUCT_KEY_ID, clickedProduct.id)
                }

            }
        )
}