package br.com.breno.orgs.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.breno.orgs.databinding.ProdutoItemBinding
import br.com.breno.orgs.model.Product

class ProductListAdapter(
    private val context: Context,
    products: List<Product>,
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private val products = products.toMutableList()

    class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.nome
        private val description = binding.descricao
        private val value = binding.valor

        fun vincula(product: Product) {
            name.text = product.name
            description.text = product.description
            value.text = product.value.toPlainString()
        }
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
