package com.skorik05.autodoctest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.skorik05.autodoctest.R
import com.skorik05.autodoctest.databinding.ItemRepositoryBinding
import com.skorik05.autodoctest.model.items.RepositoryItem
import com.skorik05.autodoctest.presentation.fragments.RepositoriesFragmentDirections
import com.squareup.picasso.Picasso

class RepositoryAdapter(private val context: Context, private val picasso: Picasso): RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    var repositoriesList = arrayListOf<RepositoryItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val itemBinding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(itemBinding)
    }

    inner class RepositoryViewHolder(itemView: ItemRepositoryBinding): RecyclerView.ViewHolder(itemView.root){

        val tvName = itemView.tvName
        val tvDescription = itemView.tvDescription
        val tvLastUpdate = itemView.tvLastUpdate
        val tvLanguages = itemView.tvLanguages
        val tvStargazersCount = itemView.tvStargazersCount
        val ivUserAvatar = itemView.ivUserAvatar
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositoriesList[position]
        with(holder) {
            tvName.text = repository.name
            tvDescription.text = repository.description
            tvLanguages.text = repository.languages
            tvLastUpdate.text = context.getString(R.string.updated_at, repository.getFormattedDate())

            tvStargazersCount.text = repository.stargazersCount.toString()
            repository.owner?.let { user ->
                picasso.load(user.avatarUrl).into(ivUserAvatar)
                user.url?.let { userUrl ->
                    itemView.setOnClickListener {
                        val action = RepositoriesFragmentDirections.actionOpenUserPage(userUrl)
                        itemView.findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun getItemCount() = repositoriesList.size
}