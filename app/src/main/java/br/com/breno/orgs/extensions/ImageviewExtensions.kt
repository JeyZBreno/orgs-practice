package br.com.breno.orgs.extensions

import android.widget.ImageView
import br.com.breno.orgs.R
import coil.load

fun ImageView.tryLoadImage(url: String? = null) {
    load(url) {
        fallback(R.drawable.erro)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}