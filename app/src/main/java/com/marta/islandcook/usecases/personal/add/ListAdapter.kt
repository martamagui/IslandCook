package com.marta.islandcook.usecases.personal.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.databinding.ItemIngredientBinding
import com.marta.islandcook.model.response.Ingredient
import kotlin.reflect.KFunction1

class ListAdapter(private val ingredientObjs: List<IngredientObj>, private val removeIngredient: (IngredientObj) -> Unit) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredientObjs[position]
        holder.binding.tvIngredient.text = ingredient.name
        holder.binding.tvQuantity.text = ingredient.quantity
        holder.binding.btndeleteIngredient.setOnClickListener {
            Log.d("ingredient - Adapter Class",ingredient.toString())

            removeIngredient(ingredient)
        }
    }

    override fun getItemCount(): Int = ingredientObjs.size

    inner class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)
}

