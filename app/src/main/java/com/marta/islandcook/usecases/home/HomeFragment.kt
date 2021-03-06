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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: HomeFragmentViewModel by viewModels()
    private var likedRecipes: MutableList<String> = mutableListOf()
    private val adapterTopRecipes: HomeListAdapter =
        HomeListAdapter({ navigateToRecipeDetail(it) }, { likeDislike(it) }, { isItLiked(it) })
    private val adapterDinnerRecipes: HomeListAdapter =
        HomeListAdapter({ navigateToRecipeDetail(it) }, { likeDislike(it) }, { isItLiked(it) })
    private val adapterPastaRecipes: HomeListAdapter =
        HomeListAdapter(
            { navigateToRecipeDetail(it) },
            { likeDislike(it) },
            { isItLiked(it) })


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
            viewModel.dbRecipes()
            viewModel.homeUIState.collect { homeUIState ->
                renderUIState(homeUIState)
            }
        }
        viewModel.getRecipesFromAPI()
        setUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///------------------------ SET UI
    private fun setUI() {
        setAdapter()
        setBtn()
    }

    private fun setAdapter() {
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

    private fun setBtn() {
        with(binding) {
            chipAll.setOnClickListener { navigateToRecipeList("") }
            chipBreaksfast.setOnClickListener { navigateToRecipeList("Breakfast") }
            chipCheese.setOnClickListener { navigateToRecipeList("Cheese") }
            chipDessert.setOnClickListener { navigateToRecipeList("Dessert") }
            chipDinner.setOnClickListener { navigateToRecipeList("Dinner") }
            chipEasy.setOnClickListener { navigateToRecipeList("easy") }
            chipHard.setOnClickListener { navigateToRecipeList("hard") }
            chipLunch.setOnClickListener { navigateToRecipeList("Lunch") }
            chipMedium.setOnClickListener { navigateToRecipeList("medium") }
            chipPasta.setOnClickListener { navigateToRecipeList("Pasta") }
            chipPotatoes.setOnClickListener { navigateToRecipeList("Potatoes") }
        }
    }

    private fun submitRecipesToAdapters(list: List<RecipeResponse>) {
        val shortList = list.shuffled().subList(0, 10)

        val dinnerList: MutableList<RecipeResponse> = mutableListOf()
        list.forEach {
            if (it.tags.contains("Dinner")) {
                dinnerList.add(it)
            }
        }
        dinnerList.shuffle()
        val pastaList: MutableList<RecipeResponse> = mutableListOf()
        list.forEach {
            if (it.tags.contains("Pasta")) {
                pastaList.add(it)
            }
        }
        pastaList.shuffle()
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
        if(state.likedRecipies?.size!=likedRecipes.size){
            if(state.likedRecipies!=null){
                likedRecipes = (state.likedRecipies as MutableList<String>?)!!
            }
        }
    }

    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexi??n.\nInt??ntalo de nuevo m??s tarde.")
            .setPositiveButton("Okay, Polisha") { dialog, which -> }
            .show()
    }


    //------------------------ NAVIGATION
    private fun navigateToRecipeList(filter: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeListFragment(filter)
        findNavController().navigate(action)
    }

    private fun navigateToRecipeDetail(item: RecipeResponse) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(item.id)
        findNavController().navigate(action)
    }

    //------------------------ DB REQUEST

    private fun isItLiked(item: RecipeResponse): Boolean {
        return likedRecipes.contains(item.id)
    }

    private fun likeDislike(item: RecipeResponse) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            if (likedRecipes.contains(item.id)) {
                viewModel.dislike(item)
                likedRecipes.remove(item.id)
            } else {
                viewModel.saveRecipe(item)
                likedRecipes.add(item.id)
            }
        }
    }
}