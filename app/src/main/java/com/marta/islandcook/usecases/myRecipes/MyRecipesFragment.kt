package com.marta.islandcook.usecases.myRecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.databinding.FragmentMyRecipesBinding
import com.marta.islandcook.provider.db.entities.Recipies
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MyRecipesFragment : Fragment() {

    private var _binding: FragmentMyRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyRecipesFragmentViewModel by viewModels()
    private val adapter: MyRecipesFromDBAdapter =
        MyRecipesFromDBAdapter { navigateToRecipeDetail(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.myRecipesUIState.collect { myRecipesUIState ->
                renderUIState(myRecipesUIState)
            }
        }
        binding.btnAddIngredient.setOnClickListener {
            val action =
                MyRecipesFragmentDirections.actionMyRecipesFragmentToAddEditRecipeFragment("hi")
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            setAdapter()
            viewModel.getRecipesFromDB()
            viewModel.myRecipesUIState.collect { myRecipesUIState ->
                renderUIState(myRecipesUIState)
            }
        }
        viewModel.getRecipesFromDB()
        lifecycleScope.launch(Dispatchers.IO) {
            setAdapter()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setAdapter() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(context, 2)
    }

    private fun submitRecipesToAdapter(recipesList : List<Recipies>) {
        adapter.submitList(recipesList)
    }

    private fun navigateToRecipeDetail(item: String) {
        val action =
            MyRecipesFragmentDirections.actionMyRecipesFragmentToRecipeDetailFragment(item)
        findNavController().navigate(action)
    }
    ///------------------------ UISTATE RELATED

    private suspend fun renderUIState(state: MyRecipesUIState) = withContext(Dispatchers.Main) {
        if (state.isLoading) {
            binding.rv.visibility = View.VISIBLE
        }
        if (state.isError) {
            showError()
        }
        if (state.isSuccess) {
            submitRecipesToAdapter(state.recipeListDB!!)
        }

    }
    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Recetas no encontradas.")
            .setPositiveButton("Vale") { dialog, which ->
            }
            .show()
    }


}