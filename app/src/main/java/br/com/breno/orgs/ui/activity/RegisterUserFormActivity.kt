package br.com.breno.orgs.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.breno.orgs.databinding.ActivityRegisterUserFormBinding
import br.com.breno.orgs.model.User

class RegisterUserFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterUserFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerButtonConfig()
    }

    private fun registerButtonConfig() {
        binding.activityRegisterButtonForm.setOnClickListener {
            val newUser = createsUser()
            Log.i("RegisterUser", "onCreate: $newUser")
            finish()
        }
    }

    private fun createsUser(): User {
        val user = binding.activityRegisterUserForm.text.toString()
        val name = binding.activityRegisterNameForm.text.toString()
        val password = binding.activityRegisterPasswordForm.text.toString()
        return User(user, name, password)
    }
}