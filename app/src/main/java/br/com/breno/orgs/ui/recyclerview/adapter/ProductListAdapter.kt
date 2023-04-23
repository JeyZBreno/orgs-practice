package br.com.breno.orgs.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.breno.orgs.databinding.ProdutoItemBinding
import br.com.breno.orgs.extensions.tryLoadImage
import br.com.breno.orgs.model.Product
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class ProductListAdapter(
    private val context: Context,
    products: List<Product>,
    var whenClickOnItem: (product: Product) -> Unit = {},
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    private val products = products.toMutableList()

    inner class ViewHolder(private val binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.nome
        private val description = binding.descricao
        private val value = binding.valor

        private lateinit var product: Product

        init {
            itemView.setOnClickListener {
                if (::product.isInitialized)
                    whenClickOnItem(product)
            }
        }

        fun vincula(product: Product) {
            this.product = product
            name.text = product.name
            description.text = product.description
            value.text = formatToBrazilCurrency(product.value)
            binding.imageView.tryLoadImage(product.image)

            /** This commited code set the visibility for the image. **/
            val visibility = if(!product.image.isNullOrBlank()) View.VISIBLE else View.GONE

            binding.imageView.visibility = visibility

        }

        private fun formatToBrazilCurrency(value: BigDecimal): String =
            NumberFormat
                .getCurrencyInstance(
                    Locale("pt", "br")
                ).format(value)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ProdutoItemBinding
            .inflate(
                LayoutInflater.from(context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val product = products[position]
        holder.vincula(product)
    }

    override fun getItemCount(): Int = products.size

    fun update(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

}
