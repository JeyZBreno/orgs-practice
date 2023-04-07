package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.R
import br.com.breno.orgs.dao.ProductList
import br.com.breno.orgs.databinding.ActivityProductFormBinding
import br.com.breno.orgs.model.Product
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {

    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveButtonConfig()
        setContentView(binding.root)
    }

    private fun saveButtonConfig() {
        val saveButton = binding.saveButton
        val dao = ProductList()

        saveButton.setOnClickListener {
            val newProduct = createProduct()
            dao.addProducts(newProduct)
            finish()
        }
    }

    private fun createProduct(): Product {
        val fieldName = binding.inputNome
        val name = fieldName.text.toString()
        val fieldDescription = binding.inputDescricao
        val description = fieldDescription.text.toString()
        val fieldValue = binding.inputValor
        val textValue = fieldValue.text.toString()
        val value = if (textValue.isBlank()) BigDecimal.ZERO else BigDecimal(textValue)

        return Product(
            name = name,
            description = description,
            value = value
        )
    }
}