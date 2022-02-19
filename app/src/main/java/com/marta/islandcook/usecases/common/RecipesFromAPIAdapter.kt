package com.marta.islandcook.usecases.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.R
import com.marta.islandcook.databinding.ItemRecipeSmallBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.utils.imageUrl

class RecipesFromAPIAdapter(private val onPictureClicked: (RecipeResponse) -> Unit,
                            private val onLikeClick: (RecipeResponse) -> Unit,
                            private val liked: Boolean) : ListAdapter<RecipeResponse, RecipesFromAPIAdapter.RecipesFromAPIViewHolder>(RecipiesAPIItemCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesFromAPIViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecipesFromAPIViewHolder, position: Int) {
        val recipe: RecipeResponse = getItem(position)
        with(holder.binding) {
            ivRecipceSmall.imageUrl(recipe.pictureUrl)
            tvSmallItem.text = recipe.name
            isliked(holder, liked)
            ivRecipceSmall.setOnClickListener {
                onPictureClicked(recipe)
            }
            ibLikeSmall.setOnClickListener {
                onLikeClick(recipe)
                isliked(holder, liked)
            }
        }
    }
    private fun isliked(holder: RecipesFromAPIViewHolder, liked: Boolean) {
        with(holder.binding) {
            if (liked) {
                ibLikeSmall.setImageResource(R.drawable.ic_baseline_favorite_35)
            } else {
                ibLikeSmall.setImageResource(R.drawable.ic_baseline_favorite_border_35)
            }
        }
    }
    inner class RecipesFromAPIViewHolder(val binding: ItemRecipeSmallBinding) :
        RecyclerView.ViewHolder(binding.root)
}
object RecipiesAPIItemCallBack : DiffUtil.ItemCallback<RecipeResponse>() {
    override fun areItemsTheSame(oldItem: RecipeResponse, newItem: RecipeResponse): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: RecipeResponse, newItem: RecipeResponse): Boolean {
        return oldItem.name == newItem.name
    }
}