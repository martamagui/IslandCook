package com.marta.islandcook.usecases.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.R
import com.marta.islandcook.databinding.ItemRecipeSmallBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.utils.imageUrl

class RecipesFromDBAdapter(
    private val onPictureClicked: (Recipies) -> Unit,
    private val onLikeClick: (Recipies) -> Unit,
    private val liked: Boolean
) : ListAdapter<Recipies, RecipesFromDBAdapter.RecipesFromDBViewHolder>(RecipiesItemCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesFromDBViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRecipeSmallBinding =
            ItemRecipeSmallBinding.inflate(inflater, parent, false)
        return RecipesFromDBViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipesFromDBViewHolder, position: Int) {
        val recipe: Recipies = getItem(position)
        with(holder.binding) {
            ivRecipceSmall.imageUrl(recipe.picture_url)
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

object RecipiesItemCallBack : DiffUtil.ItemCallback<Recipies>() {
    override fun areItemsTheSame(oldItem: Recipies, newItem: Recipies): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }
    override fun areContentsTheSame(oldItem: Recipies, newItem: Recipies): Boolean {
        return oldItem.name == newItem.name
    }
}
