package br.com.breno.orgs.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.breno.orgs.databinding.UserHeaderBinding

class HeaderAdapter(
    private val context: Context,
    user: List<String?> = emptyList()
) : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    private val user: List<String?> = user.toMutableList()

    class ViewHolder(
        private val binding: UserHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun vincula(user: String?) {
            binding.userHeaderId.text = user
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ViewHolder(
        UserHeaderBinding
            .inflate(
                LayoutInflater.from(context),
                parent,
                false,
            )
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val user = if (user[position].isNullOrBlank())
            "Sem Usuário"
        else user[position]
        holder.vincula(user)
    }

    override fun getItemCount(): Int = user.size
}