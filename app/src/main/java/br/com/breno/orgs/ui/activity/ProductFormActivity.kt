package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.R
import br.com.breno.orgs.dao.ProductList
import br.com.breno.orgs.databinding.ActivityProductFormBinding
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.dialog.ImageFormDialog
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {

    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveButtonConfig()
        switchImageConfig()
        setContentView(binding.root)
        title = "Cadastrar produto"
    }

    private var url: String? = null

    private fun switchImageConfig() {
        val switchImage = binding.chosenImage

        switchImage.setOnClickListener {
            ImageFormDialog(this)
                .showThem(urlDefault = url) { image ->
                    url = image
                    binding.chosenImage.tryLoadImage(url)
                }

        }
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
        val fieldName = binding.inputName
        val name = fieldName.text.toString()
        val fieldDescription = binding.inputDescription
        val description = fieldDescription.text.toString()
        val fieldValue = binding.inputValue
        val textValue = fieldValue.text.toString()
        val value = if (textValue.isBlank()) BigDecimal.ZERO else BigDecimal(textValue)

        return Product(
            name = name,
            description = description,
            value = value,
            image = url,
        )
    }
}