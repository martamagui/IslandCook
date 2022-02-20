package com.marta.islandcook.usecases.myRecipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.databinding.FragmentMyRecipesBinding
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.usecases.common.RecipesFromDBAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyRecipesFragment : Fragment() {

    private var _binding: FragmentMyRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipesFragmentViewModel by viewModels()
    private var recipesList: MutableList<Recipies> = mutableListOf()
    private val adapter: RecipesFromDBAdapter =
        RecipesFromDBAdapter(
            { navigateToRecipeDetail(it) },
            { likeDislike(it) },
            true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddIngredient.setOnClickListener{
            val action = MyRecipesFragmentDirections.actionMyRecipesFragmentToAddEditRecipeFragment("")
            findNavController().navigate(action)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            getLikedRecipes()
            viewModel.myRecipesUIState.collect { myRecipesUIState ->
                renderUIState(myRecipesUIState)
            }
        }
        setUI()
        lifecycleScope.launch(Dispatchers.IO) {
            getLikedRecipes()
            withContext(Dispatchers.Main){
                submitRecipesToAdapter()
            }
        }




    }
    private fun setUI() {
        setAdapter()
    }
    private fun setAdapter() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(context,2)
    }
    private fun submitRecipesToAdapter() {
        adapter.submitList(recipesList)
    }
    private fun navigateToRecipeDetail(item: Recipies) {
        val action =
            MyRecipesFragmentDirections.actionMyRecipesFragmentToRecipeDetailFragment(item.recipeId)
        findNavController().navigate(action)
    }
    ///------------------------ UISTATE RELATED

    private suspend fun renderUIState(state: MyRecipesUIState) = withContext(Dispatchers.Main) {
        if (state.isLoading) {
            //TODO
        }
        if (state.isError) {
            //TODO
            showError()
        }
        if (state.isSuccess) {
            submitRecipesToAdapter()
            //TODO ask how to acces DB from ViewModel with out context
        }
        //TODO Need to hide que msg for the empty cases if the list has items
    }
    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Recetas no encontradas.")
            .setPositiveButton("Vale") { dialog, which ->
            }
            .show()
    }

    //------------------------ DB REQUEST
    private suspend fun getLikedRecipes() {
        recipesList =
            IslandCook_Database.getInstance(requireContext()).recipiesDao().findByMyRecipies(true).toMutableList()
    }

    private fun likeDislike(item: Recipies) {
        lifecycleScope.launch(Dispatchers.IO) {
            dislike(item)
            getLikedRecipes()
            withContext(Dispatchers.Main){
                submitRecipesToAdapter()
            }
        }
    }



    private suspend fun dislike(item: Recipies) {
        IslandCook_Database.getInstance(requireContext()).recipiesDao().deleteRecipie(item)
    }
}