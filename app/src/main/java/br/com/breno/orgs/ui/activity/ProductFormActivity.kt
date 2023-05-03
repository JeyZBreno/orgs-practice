package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.R
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductFormBinding
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.dialog.ImageFormDialog
import br.com.breno.orgs.utils.KEY_PRODUCT
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {

    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveButtonConfig()
        switchImageConfig()
        setContentView(binding.root)
        title = "Cadastrar produto"
        tryLoadProduct()
    }

    private var url: String? = null
    private var productId = 0L

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
        val orgsDb = OrgsDatabase.getInstance(this)
        val productDao = orgsDb.productDao()
        saveButton.setOnClickListener {
            val newProduct = createProduct()
            if(productId > 0)
                productDao.updateItem(newProduct)
            else
                productDao.save(newProduct)
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
            id = productId,
            name = name,
            description = description,
            value = value,
            image = url,
        )
    }

    private fun tryLoadProduct() {
        intent.getParcelableExtra<Product>(KEY_PRODUCT)?.let { loadedProduct ->
            title = "Editar produto"
            productId = loadedProduct.id
            url = loadedProduct.image
            with(binding) {
                chosenImage.tryLoadImage(loadedProduct.image)
                inputName.setText(loadedProduct.name)
                inputDescription.setText(loadedProduct.description)
                inputValue.setText(loadedProduct.value.toPlainString())
            }
        }
    }
}