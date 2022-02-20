package com.marta.islandcook.usecases.personal.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marta.islandcook.databinding.ItemStepsBinding

class ListAdapterSteps(private val step: List<String>, private val removeStep: (String) -> Unit) : ListAdapter<String, ListAdapterSteps.ViewHolder>(StepsItemCallback) {
    inner class ViewHolder(val binding: ItemStepsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemStepsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = step.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = getItem(position)
        holder.binding.tvStep.text = step

        holder.binding.btnDeleteStep.setOnClickListener {
            Log.d("ingredient - Adapter Class",step.toString())
            removeStep(step)
        }
    }


}

object StepsItemCallback : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}


