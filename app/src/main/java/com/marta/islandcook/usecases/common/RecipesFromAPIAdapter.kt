package com.marta.islandcook.usecases.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.R
import com.marta.islandcook.databinding.ItemRecipeSmallBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.utils.imageUrl

class RecipesFromAPIAdapter(
    private val onPictureClicked: (RecipeResponse) -> Unit,
    private val onLikeClick: (RecipeResponse) -> Unit,
    private val liked: (RecipeResponse) -> Boolean
) : ListAdapter<RecipeResponse, RecipesFromAPIAdapter.RecipesFromAPIViewHolder>(
    RecipiesAPIItemCallBack
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesFromAPIViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRecipeSmallBinding =
            ItemRecipeSmallBinding.inflate(inflater, parent, false)
        return RecipesFromAPIViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipesFromAPIViewHolder, position: Int) {
        val recipe: RecipeResponse = getItem(position)
        var isliked = liked(recipe)
        with(holder.binding) {
            ivRecipceSmall.imageUrl(recipe.pictureUrl)
            tvSmallItem.text = recipe.name
            isliked(holder, liked(recipe))
            ivRecipceSmall.setOnClickListener {
                onPictureClicked(recipe)
            }
            ibLikeSmall.setOnClickListener {
                onLikeClick(recipe)
                if(isliked){
                    isliked = false
                }else{
                    isliked = true
                }
                isliked(holder, isliked)
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