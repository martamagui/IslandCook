package com.marta.islandcook.usecases.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.databinding.FragmentHomeBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.usecases.common.HomeListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: HomeFragmentViewModel by viewModels()

    //TODO función de like
    private val adapterTopRecipes: HomeListAdapter =
        HomeListAdapter({ navigateToRecipeDetail(it) }, { saveRecipe(it) }, false)
    private val adapterDinnerRecipes: HomeListAdapter =
        HomeListAdapter({ navigateToRecipeDetail(it) }, { saveRecipe(it) }, false)
    private val adapterPastaRecipes: HomeListAdapter =
        HomeListAdapter({ navigateToRecipeDetail(it) }, { saveRecipe(it) }, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.homeUIState.collect { homeUIState ->
                renderUIState(homeUIState)
            }
        }
        setUI()
        viewModel.getRecipes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///------------------------ SET UI
    fun setUI() {
        setAdapter()
        setBtn()
    }

    fun setAdapter() {
        binding.rvTopRecipes.adapter = adapterTopRecipes
        binding.rvTopRecipes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTopRecipesDinner.adapter = adapterDinnerRecipes
        binding.rvTopRecipesDinner.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTopRecipesPasta.adapter = adapterPastaRecipes
        binding.rvTopRecipesPasta.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setBtn() {
        with(binding) {
            chipAll.setOnClickListener { navigateToRecipeList("All") }
            chipBreaksfast.setOnClickListener { navigateToRecipeList("Breaksfast") }
            chipCheese.setOnClickListener { navigateToRecipeList("Cheese") }
            chipDessert.setOnClickListener { navigateToRecipeList("Dessert") }
            chipDinner.setOnClickListener { navigateToRecipeList("Dinner") }
            chipEasy.setOnClickListener { navigateToRecipeList("Easy") }
            chipHard.setOnClickListener { navigateToRecipeList("Hard") }
            chipLunch.setOnClickListener { navigateToRecipeList("Lunch") }
            chipMedium.setOnClickListener { navigateToRecipeList("Medium") }
            chipPasta.setOnClickListener { navigateToRecipeList("Pasta") }
            chipPotatoes.setOnClickListener { navigateToRecipeList("Potatoes") }
        }
    }

    fun submitRecipesToAdapters(list: List<RecipeResponse>) {
        val shortList = list.shuffled().subList(0, 10)
        val dinnerList: MutableList<RecipeResponse> = mutableListOf()
        list.forEach {
            if (it.tags.contains("Dinner")) {
                dinnerList.add(it)
            }
        }
        val pastaList: MutableList<RecipeResponse> = mutableListOf()
        list.forEach {
            if (it.tags.contains("Pasta")) {
                pastaList.add(it)
            }
        }
        adapterTopRecipes.submitList(shortList)
        adapterDinnerRecipes.submitList(dinnerList)
        adapterPastaRecipes.submitList(pastaList)
    }
    ///------------------------ UISTATE RELATED

    private suspend fun renderUIState(state: HomeUIState) = withContext(Dispatchers.Main) {
        if (state.isLoading) {
            binding.shimmerRvTopRecipes.visibility = View.VISIBLE
            binding.shimmerRvTopRecipesDinner.visibility = View.VISIBLE
            binding.shimmerRvTopRecipesPasta.visibility = View.VISIBLE
        }
        if (state.isError) {
            showError()
        }
        if (state.isSuccess) {
            submitRecipesToAdapters(state.recipeList!!)
            binding.shimmerRvTopRecipes.visibility = View.GONE
            binding.shimmerRvTopRecipesDinner.visibility = View.GONE
            binding.shimmerRvTopRecipesPasta.visibility = View.GONE
        }
    }

    fun showError(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexión.\nIntántalo de nuevo más tarde")
            .setPositiveButton("Okay, Polisha") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }


    //------------------------ NAVIGATION
    fun navigateToRecipeList(filter: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeListFragment(filter)
        findNavController().navigate(action)
    }

    fun navigateToRecipeDetail(item: RecipeResponse) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(item.id)
        findNavController().navigate(action)
    }

    //------------------------ NAVIGATION
    fun saveRecipe(item: RecipeResponse){
        //TODO Guardar recetaBD
    }

}