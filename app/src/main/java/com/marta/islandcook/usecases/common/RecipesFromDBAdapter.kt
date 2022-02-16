package com.marta.islandcook.usecases.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.R
import com.marta.islandcook.databinding.ItemRecipeSmallBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.utils.imageUrl

class RecipesFromDBAdapter(
    private val onPictureClicked: (RecipeResponse) -> Unit,
    private val onLikeClick: (RecipeResponse) -> Unit,
    private val liked: (RecipeResponse) -> Boolean
) : ListAdapter<RecipeResponse, RecipesFromDBAdapter. RecipesFromDBViewHolder>(RecipeItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesFromDBViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRecipeSmallBinding = ItemRecipeSmallBinding.inflate(inflater, parent, false)
        return RecipesFromDBViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecipesFromDBViewHolder, position: Int) {
        val recipe = getItem(position)
        var liked = liked(recipe)
        with(holder.binding) {
            ivRecipceSmall.imageUrl(recipe.pictureUrl)
            tvSmallItem.text = recipe.name
            isliked(holder, liked)
            ivRecipceSmall.setOnClickListener {
                onPictureClicked(recipe)
            }
            ibLikeSmall.setOnClickListener {
                onLikeClick(recipe)
                if (liked) {
                    liked = false
                } else {
                    liked = true
                }
                isliked(holder, liked)
            }
        }
    }
    private fun isliked(holder: RecipesFromDBViewHolder, liked: Boolean) {
        with(holder.binding) {
            if (liked) {
                ibLikeSmall.setImageResource(R.drawable.ic_baseline_favorite_35)
            } else {
                ibLikeSmall.setImageResource(R.drawable.ic_baseline_favorite_border_35)
            }
        }
    }
    inner class RecipesFromDBViewHolder(val binding: ItemRecipeSmallBinding) :
        RecyclerView.ViewHolder(binding.root)
}