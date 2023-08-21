package br.com.breno.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.breno.orgs.R
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductFormBinding
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.ui.dialog.ImageFormDialog
import br.com.breno.orgs.utils.PRODUCT_KEY_ID
import br.com.breno.orgs.utils.preferences.dataStore
import br.com.breno.orgs.utils.preferences.loggedUserPreferences
import java.math.BigDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFormActivity : BaseUserActivity() {

    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }
    private val productDao by lazy { OrgsDatabase.getInstance(this).productDao() }
    private val userDao by lazy { OrgsDatabase.getInstance(this).userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveButtonConfig()
        switchImageConfig()
        setContentView(binding.root)
        title = "Cadastrar produto"
        tryLoadProduct()
        findByProduct()
        lifecycleScope.launch {
            user
                .filterNotNull()
                .collect {
                    Log.i("ProductForm", "onCreate: $it")
                }
        }
    }

    private fun findByProduct() {
        lifecycleScope.launch {
            productDao.findById(productId).collect { product ->
                product?.let { foundProduct ->
                    fillFields(foundProduct)
                }
            }
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
            lifecycleScope.launch {
                productDao.save(newProduct)
                finish()
            }
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