package br.com.breno.orgs.ui.activity

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityProductFormBinding
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import br.com.breno.orgs.model.User
import br.com.breno.orgs.ui.dialog.ImageFormDialog
import br.com.breno.orgs.utils.PRODUCT_KEY_ID
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.math.BigDecimal

class ProductFormActivity : BaseUserActivity() {

    private val binding by lazy { ActivityProductFormBinding.inflate(layoutInflater) }
    private val productDao by lazy { OrgsDatabase.getInstance(this).productDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveButtonConfig()
        switchImageConfig()
        setContentView(binding.root)
        title = "Cadastrar produto"
        tryLoadProduct()
        findByProduct()
    }

    private fun findByProduct() {
        lifecycleScope.launch {
            productDao.findById(productId).collect { product ->
                product?.let { foundProduct ->
                    val userIdField = binding.userId
                    userIdField.visibility = if (foundProduct.saveWithoutUser()) {
                        userFieldConfig()
                        VISIBLE
                    } else
                        GONE
                    fillFields(foundProduct)
                }
            }
        }
    }

    private fun userFieldConfig() {
        lifecycleScope.launch {
            users().map { users ->
                users.map { it.id }
            }.collect { users ->
                autoCompleteViewConfig(users)
            }
        }
    }

    private fun autoCompleteViewConfig(
        users: List<String>,
    ) {
        val userFieldId = binding.userId
        val adapter = ArrayAdapter(
            this@ProductFormActivity,
            R.layout.simple_dropdown_item_1line,
            users,
        )
        userFieldId.setAdapter(adapter)
        userFieldId.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validUser(users)
            }
        }
    }

    private fun validUser(
        users: List<String>,
    ): Boolean {
        val userFieldId = binding.userId
        val userId = userFieldId.text.toString()
        return if (!users.contains(userId)) {
            userFieldId.error = "Usuário Inexistente"
            false
        } else true
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
            lifecycleScope.launch {
                tryToSaveProduct()
            }
        }
    }

    private suspend fun tryToSaveProduct() {
        user.value?.let { user ->
            try {
                val userId = defineUserId(user)
                val product = createProduct(userId)
                productDao.save(product)
                finish()
            } catch (e: RuntimeException) {
                Log.e("ProductForm", "saveButtonCOnfig: ", e)
            }
        }
    }

    private suspend fun defineUserId(user: User): String = productDao
        .findById(productId)
        .first()?.let { foundProduct ->
            if (foundProduct.userId.isNullOrBlank()) {
                val users = users()
                    .map { foundUsers ->
                        foundUsers.map { it.id }
                    }.first()
                if (validUser(users)) {
                    val userFieldId = binding.userId
                    return userFieldId.text.toString()
                } else
                    throw RuntimeException("Tentou salvar produto com usuário inexistente")
            }
            null
        } ?: user.id

    private fun createProduct(userId: String): Product {
        val fieldName = binding.inputName
        val name = fieldName.text.toString()
        val fieldDescription = binding.inputDescription
        val description = fieldDescription.text.toString()
        val fieldValue = binding.inputValue
        val textValue = fieldValue.text.toString()
        val value = if (textValue.isBlank()) BigDecimal.ZERO else BigDecimal(textValue)

        lifecycleScope.launch {
            user
                .filterNotNull()
                .collect {
                    Log.i("ProductForm", "onCreate: $it")
                }
        }

        return Product(
            id = productId,
            name = name,
            description = description,
            value = value,
            image = url,
            userId = userId
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