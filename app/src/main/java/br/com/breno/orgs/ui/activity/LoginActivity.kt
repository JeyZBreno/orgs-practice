package br.com.breno.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityLoginBinding
import br.com.breno.orgs.extensions.parseTo
import br.com.breno.orgs.extensions.toast
import br.com.breno.orgs.utils.preferences.dataStore
import br.com.breno.orgs.utils.preferences.loggedUserPreferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val userDao by lazy {
        OrgsDatabase.getInstance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerButtonConfig()
        enterButtonConfig()
    }

    private fun enterButtonConfig() {
        binding.activityLoginEnterButton.setOnClickListener {
            val user = binding.activityLoginUser.text.toString()
            val password = binding.activityLoginSenha.text.toString()
            authUser(user, password)
        }
    }

    private fun authUser(user: String, password: String) {
        lifecycleScope.launch {
            userDao.authUser(user, password)?.let { user ->
                dataStore.edit {preferences ->
                    preferences[loggedUserPreferences] = user.id
                }
                Log.i("LoginActivity", "onCreate: $user - $password")
                parseTo(ProductListActivity::class.java)
                finish()
            } ?: toast("Falha na autenticação")
        }
    }

    private fun registerButtonConfig() {
        binding.activityLoginRegisterButton.setOnClickListener {
            parseTo(RegisterUserFormActivity::class.java)
        }
    }
}