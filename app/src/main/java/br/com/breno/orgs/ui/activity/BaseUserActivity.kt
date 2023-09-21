package br.com.breno.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.breno.orgs.database.OrgsDatabase
import br.com.breno.orgs.extensions.goTo
import br.com.breno.orgs.model.User
import br.com.breno.orgs.utils.preferences.dataStore
import br.com.breno.orgs.utils.preferences.loggedUserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class BaseUserActivity : AppCompatActivity() {

    private val userDao by lazy {
        OrgsDatabase.getInstance(this).userDao()
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    protected val user: StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            verifyLoggedUser()
        }
    }

    private suspend fun verifyLoggedUser() {
        dataStore.data.collect {preferences ->
            preferences[loggedUserPreferences]?.let {userId ->
                findUser(userId)
            } ?: gotToLogin()
        }
    }

    private suspend fun findUser(userId: String) : User? {
        return userDao
            .findById(userId)
            .firstOrNull().also { user ->
                _user.value = user
            }
    }

    protected suspend fun logOutUser() {
        dataStore.edit { preferences ->
            preferences.remove(loggedUserPreferences)
        }
    }

    private fun gotToLogin() {
        goTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    protected fun users() = userDao.findAll()
}