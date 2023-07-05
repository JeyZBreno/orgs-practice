package br.com.breno.orgs.extensions

import android.content.Context
import android.content.Intent

fun Context.parseTo(clazz: Class<*>) {
    Intent(this, clazz).apply {
        startActivity(this)
    }
}