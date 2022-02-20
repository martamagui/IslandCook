package com.marta.islandcook.usecases.personal.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.databinding.ItemIngredientBinding
import com.marta.islandcook.model.response.Ingredient

class ListAdapterIngredients(private val ingredient: List<Ingredient>, private val removeIngredient: (Ingredient) -> Unit) : ListAdapter<Ingredient,ListAdapterIngredients.ViewHolder>(IngredientItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredient.size

    inner class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val ingredient = getItem(position)
        holder.binding.tvIngredient.text = ingredient.name
        holder.binding.tvQuantity.text = ingredient.amount
        holder.binding.btnDeleteIngredient.setOnClickListener {
            Log.d("ingredient - Adapter Class",ingredient.toString())
            removeIngredient(ingredient)
        }
    }
}

object IngredientItemCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean =
        oldItem == newItem
}
