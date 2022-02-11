package com.marta.islandcook.usecases.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.R
import com.marta.islandcook.databinding.ItemRecipeHomeBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.utils.imageUrl

class HomeListAdapter(private val onPictureClicked:(RecipeResponse)->Unit, private val onLikeClick:(RecipeResponse)->Unit, private val liked: Boolean) : ListAdapter<RecipeResponse, HomeListAdapter.HomeListViewHolder>(RecipeItemCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRecipeHomeBinding = ItemRecipeHomeBinding.inflate(inflater, parent, false)
        return HomeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        val recipe = getItem(position)
        with(holder.binding){
            ivRecipce.imageUrl(recipe.pictureUrl)
            ivRecipce.setOnClickListener {
                //TODO añadir navigateTo
            }
            ibLike.setOnClickListener{
                //TODO añadir funcion de like
            }
            if(liked){
                ibLike.setImageResource(R.drawable.ic_baseline_favorite_35)
            }
        }
    }

    inner class HomeListViewHolder(val binding: ItemRecipeHomeBinding) :
        RecyclerView.ViewHolder(binding.root)
}

object RecipeItemCallback: DiffUtil.ItemCallback<RecipeResponse>() {
    override fun areItemsTheSame(oldItem: RecipeResponse, newItem: RecipeResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecipeResponse, newItem: RecipeResponse): Boolean {
        return oldItem.pictureUrl == newItem.pictureUrl
    }

}
