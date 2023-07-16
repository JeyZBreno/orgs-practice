package br.com.breno.orgs.extensions

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.parseTo(
    clazz: Class<*>,
    intent: Intent.() -> Unit = {}
) {
    Intent(this, clazz).apply {
        Intent()
        startActivity(this)
    }
}

fun Context.toast(mensagem: String) {
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_SHORT
    ).show()
}