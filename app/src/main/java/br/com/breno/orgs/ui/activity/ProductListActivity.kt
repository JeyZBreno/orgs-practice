package br.com.breno.orgs.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.breno.orgs.R
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductListBinding
import br.com.breno.orgs.extensions.goTo
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.recyclerview.adapter.ProductListAdapter
import br.com.breno.orgs.utils.PRODUCT_KEY_ID
import br.com.breno.orgs.utils.preferences.dataStore
import br.com.breno.orgs.utils.preferences.loggedUserPreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListActivity : BaseUserActivity() {

    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }
    private val productDao by lazy { OrgsDatabase.getInstance(this).productDao() }
    private val adapter = ProductListAdapter(
        context = this,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerViewConfig()
        floatingActionButtonConfig()
        lifecycleScope.launch {
            launch {
                user
                    .filterNotNull()
                    .collect { user ->
                        Log.i("ProductList", "onCreate: $user")
                        findUserProducts(user.id)
                    }
            }
        }
        setContentView(binding.root)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            val orderedProducts: List<Product>? = when (item.itemId) {
                R.id.menu_lista_produtos_ordenar_nome_asc ->
                    productDao.findAllOrderedByNameAsc()

                R.id.menu_lista_produtos_ordenar_nome_desc ->
                    productDao.findAllOrderedByNameDesc()

                R.id.menu_lista_produtos_ordenar_descricao_asc ->
                    productDao.findAllOrderedByDescriptionAsc()

                R.id.menu_lista_produtos_ordenar_descricao_desc ->
                    productDao.findAllOrderedByDescriptionDesc()

                R.id.menu_lista_produtos_ordenar_valor_asc ->
                    productDao.findAllOrderedByValueAsc()

                R.id.menu_lista_produtos_ordenar_valor_desc ->
                    productDao.findAllOrderedByValueDesc()

                else -> null
            }
            orderedProducts?.let { products ->
                adapter.update(products)
            }
            when (item.itemId) {
                R.id.menu_user_profile ->
                    navigateToUserProfile()
                R.id.menu_all_products_by_user ->
                    goTo(AllProductsActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun findUserProducts(userId: String) {
        productDao.findAllByUser(userId).collect { allProducts ->
            adapter.update(allProducts)
        }
    }

    private fun floatingActionButtonConfig() {
        val floatingActionButton = binding.floatingActionButton
        floatingActionButton.setOnClickListener {
            navigateToProductForm()
        }
    }

    private fun navigateToProductForm() {
        val intent = Intent(this, ProductFormActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun navigateToUserProfile() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun recyclerViewConfig() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.whenClickOnItem = { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_KEY_ID, product.id)
            }
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }
}