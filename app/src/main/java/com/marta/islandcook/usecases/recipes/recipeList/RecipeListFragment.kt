package com.marta.islandcook.usecases.recipes.recipeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marta.islandcook.R


class RecipeListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
        //TODO _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //TODO _binding = null
    }
    //------------------------ UI RELATED

    //------------------------ API REQUEST

    //------------------------ DB

    //------------------------ NAVIGATION
}