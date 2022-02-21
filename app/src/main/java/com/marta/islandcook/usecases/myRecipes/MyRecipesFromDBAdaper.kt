package com.marta.islandcook.usecases.myRecipes

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.R
import com.marta.islandcook.databinding.ItemMyRecipeSmallBinding
import com.marta.islandcook.databinding.ItemRecipeSmallBinding
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.usecases.common.RecipesFromDBAdapter
import com.marta.islandcook.usecases.common.RecipiesItemCallBack
import com.marta.islandcook.utils.imageUrl

class MyRecipesFromDBAdapter(private val onPictureClicked: (String) -> Unit) : ListAdapter<Recipies, MyRecipesFromDBAdapter.RecipesFromDBViewHolder>(
    MyRecipesItemCallBack
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesFromDBViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMyRecipeSmallBinding =
            ItemMyRecipeSmallBinding.inflate(inflater, parent, false)
        return RecipesFromDBViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipesFromDBViewHolder, position: Int) {
        val recipe: Recipies = getItem(position)
        with(holder.binding) {
            ivRecipceSmall.imageUrl(recipe.picture_url)
            tvSmallItem.text = recipe.name
            ivRecipceSmall.setOnClickListener{
                onPictureClicked(recipe.recipeId)
                Log.d("",recipe.recipeId )
            }
        }
    }

    inner class RecipesFromDBViewHolder(val binding: ItemMyRecipeSmallBinding) :
        RecyclerView.ViewHolder(binding.root)
}

object MyRecipesItemCallBack : DiffUtil.ItemCallback<Recipies>() {
    override fun areItemsTheSame(oldItem: Recipies, newItem: Recipies): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }
    override fun areContentsTheSame(oldItem: Recipies, newItem: Recipies): Boolean {
        return oldItem.name == newItem.name
    }
}