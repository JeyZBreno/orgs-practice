package br.com.breno.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.databinding.ActivityLoginBinding
import br.com.breno.orgs.extensions.parseTo

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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
            Log.i("LoginActivity", "onCreate: $user - $password")
            parseTo(ProductListActivity::class.java)
        }
    }

    private fun registerButtonConfig() {
        binding.activityLoginRegisterButton.setOnClickListener {
            parseTo(RegisterUserFormActivity::class.java)
        }
    }
}