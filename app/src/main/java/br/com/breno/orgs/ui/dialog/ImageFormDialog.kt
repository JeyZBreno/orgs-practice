package br.com.breno.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.breno.orgs.R
import br.com.breno.orgs.databinding.ImageFormBinding
import coil.load

class ImageFormDialog(
    private val context: Context,
) {

    fun showThem(loadedImage: (image: String) -> Unit) {
        val binding = ImageFormBinding
            .inflate(LayoutInflater.from(context))

        binding.reloadButton.setOnClickListener {
            val url = binding.imageUrl.text.toString()
            binding.imageChanged.load(url) {
                placeholder(R.drawable.placeholder)
            }
        }

        AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton("Confirmar") { _, _ ->
                val url = binding.imageUrl.text.toString()
                loadedImage(url)

            }
            .setNegativeButton("Cancelar") { _, _ ->

            }
            .show()
    }
}