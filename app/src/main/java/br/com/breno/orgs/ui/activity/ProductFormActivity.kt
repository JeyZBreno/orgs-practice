package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.R
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductFormBinding
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.dialog.ImageFormDialog
import br.com.breno.orgs.utils.PRODUCT_KEY_ID
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity(R.layout.activity_product_form) {

    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }
    private val productDao by lazy {
        OrgsDatabase
            .getInstance(this)
            .productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveButtonConfig()
        switchImageConfig()
        setContentView(binding.root)
        title = "Cadastrar produto"
        tryLoadProduct()
    }

    override fun onResume() {
        super.onResume()
        findByProduct()
    }

    private fun findByProduct() {
        productDao.findById(productId)?.let { product ->
            fillFields(product)
        }
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
        saveButton.setOnClickListener {
            val newProduct = createProduct()
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
        productId = intent.getLongExtra(PRODUCT_KEY_ID, 0L)
    }

    private fun fillFields(product: Product) {
        title = "Editar produto"
        url = product.image
        with(binding) {
            chosenImage.tryLoadImage(product.image)
            inputName.setText(product.name)
            inputDescription.setText(product.description)
            inputValue.setText(product.value.toPlainString())
        }
    }
}