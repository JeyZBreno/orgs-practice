package br.com.breno.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.breno.orgs.databinding.ActivityProfileUserBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class UserProfileActivity : BaseUserActivity() {

    private val binding by lazy {
        ActivityProfileUserBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appOutButtonConfig()
        fillFields()
    }

    private fun fillFields() {
        lifecycleScope.launch {
            user.filterNotNull().collect {loggedUser ->
                binding.activityProfileUserId.text = loggedUser.id
                binding.activitiyProfileUserName.text = loggedUser.name
            }
        }
    }

    private fun appOutButtonConfig() {
        binding.activityProfileUserFinishAppButton.setOnClickListener {
            lifecycleScope.launch {
                logOutUser()
            }
        }
    }
}