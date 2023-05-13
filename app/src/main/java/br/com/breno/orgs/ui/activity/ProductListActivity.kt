package br.com.breno.orgs.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.breno.orgs.R
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductListBinding
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.recyclerview.adapter.ProductListAdapter
import br.com.breno.orgs.utils.PRODUCT_KEY_ID
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ProductList"

class ProductListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }
    private val productDao by lazy { OrgsDatabase.getInstance(this).productDao() }
    private val job = Job()

    private val adapter = ProductListAdapter(
        context = this,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerViewConfig()
        floatingActionButtonConfig()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        //It get some exception initialized on coroutine scope
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i(TAG, "onResume: throwable $throwable")
            Toast.makeText(
                this@ProductListActivity,
                "Rolou um errinho de leve",
                Toast.LENGTH_LONG
            ).show()
        }
        lifecycleScope.launch(exceptionHandler) {
            val allProducts = productDao.findAll()
            adapter.update(allProducts)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            val orderedProducts = withContext(Dispatchers.IO) {
                when (item.itemId) {
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

                    R.id.menu_lista_produtos_ordenar_sem_ordem ->
                        productDao.findAll()

                    else -> null
                }
            }
            orderedProducts?.let { products ->
                adapter.update(products)
            }
        }
        return super.onOptionsItemSelected(item)
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