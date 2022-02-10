package com.marta.islandcook.usecases.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marta.islandcook.R


class AddEditRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO Binding
        return inflater.inflate(R.layout.fragment_add_edit_recipe, container, false)
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