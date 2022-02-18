package com.marta.islandcook.usecases.personal.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.databinding.ItemIngredientBinding

class ListAdapter(private val ingredientObjs: List<IngredientObj>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredientObjs[position]
        holder.binding.tvIngredient.text = ingredient.name
    }

    override fun getItemCount(): Int = ingredientObjs.size

    inner class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)
}

