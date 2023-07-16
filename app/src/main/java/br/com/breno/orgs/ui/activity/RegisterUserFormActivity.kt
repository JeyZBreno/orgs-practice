package br.com.breno.orgs.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.databinding.ActivityRegisterUserFormBinding
import br.com.breno.orgs.model.User
import kotlinx.coroutines.launch

class RegisterUserFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterUserFormBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        OrgsDatabase.getInstance(this).userDao()
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
            lifecycleScope.launch {
                try {
                    dao.saveUser(newUser)
                    finish()
                } catch (e: Exception) {
                    Log.i("RegisterUser", "registerButtonConfig: ", e)
                    Toast.makeText(
                        this@RegisterUserFormActivity,
                        "Falha ao cadastrar usuário",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun createsUser(): User {
        val user = binding.activityRegisterUserForm.text.toString()
        val name = binding.activityRegisterNameForm.text.toString()
        val password = binding.activityRegisterPasswordForm.text.toString()
        return User(user, name, password)
    }
}