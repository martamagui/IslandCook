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

    //TODO función de like y navigation
    private val adapter: HomeListAdapter = HomeListAdapter({}, {}, false)

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///------------------------ UI RELATED
    fun setUI() {
        setAdapter()
        setBtn()
    }

    fun setAdapter() {
        binding.rvTopRecipes.adapter = adapter
        binding.rvTopRecipes.layoutManager =
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

    fun submitRecipesToAdapter(list: List<RecipeResponse>) {
        val shortList = list.shuffled().subList(0, 10)
        adapter.submitList(shortList)
    }

    private suspend fun renderUIState(state: HomeUIState) = withContext(Dispatchers.Main) {
        if (state.isLoading) {
            //TODO Loading bar visible
        } else {
            //TODO Loading bar invisible
        }
        if (state.isError) {
            //TODO Popup error conexión
        }
        if (state.isSuccess) {
            submitRecipesToAdapter(state.recipeList!!)
        }
    }

    //------------------------ DB

    //------------------------ NAVIGATION
    fun navigateToRecipeList(filter: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeListFragment(filter)
        findNavController().navigate(action)
    }

}