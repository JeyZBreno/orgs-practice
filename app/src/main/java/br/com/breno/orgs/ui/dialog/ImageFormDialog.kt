package br.com.breno.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.breno.orgs.databinding.ImageFormBinding
import br.com.breno.orgs.extensions.tryLoadImage

class ImageFormDialog(
    private val context: Context,
) {
    fun showThem(
        urlDefault: String? = null,
        loadedImage: (image: String) -> Unit,
    ) {
        ImageFormBinding
            .inflate(LayoutInflater.from(context)).apply {
                urlDefault?.let { url ->
                    imageChanged.tryLoadImage(url)
                    imageUrl.setText(url)
                }

                reloadButton.setOnClickListener {
                    val url = imageUrl.text.toString()
                    imageChanged.tryLoadImage(url)
                }

                AlertDialog.Builder(context)
                    .setView(root)
                    .setPositiveButton("Confirmar") { _, _ ->
                        val url = imageUrl.text.toString()
                        loadedImage(url)
                    }
                    .setNegativeButton("Cancelar") { _, _ ->

                    }
                    .show()
            }
    }
}