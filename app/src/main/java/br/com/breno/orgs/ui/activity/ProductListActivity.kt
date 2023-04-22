package br.com.breno.orgs.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.breno.orgs.R
import br.com.breno.orgs.dao.ProductList
import br.com.breno.orgs.databinding.ActivityProductListBinding
import br.com.breno.orgs.ui.recyclerview.adapter.ProductListAdapter

class ProductListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }

    private val dao = ProductList()
    private val adapter = ProductListAdapter(
        context = this,
        products = dao.findAll(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerViewConfig()
        floatingActionButtonConfig()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        adapter.update(dao.findAll())
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
        Log.i("MainActivity", "onResume: ${dao.findAll()}")
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}