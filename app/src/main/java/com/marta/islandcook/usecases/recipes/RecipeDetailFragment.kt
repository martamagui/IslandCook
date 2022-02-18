package com.marta.islandcook.usecases.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentHomeBinding
import com.marta.islandcook.databinding.FragmentRecipeDetailBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.usecases.common.HomeListAdapter
import com.marta.islandcook.utils.imageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RecipeDetailFragment: Fragment() {
    private var recipe: RecipeResponse? = null
    private val args: RecipeDetailFragmentArgs by navArgs()
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
        //TODO _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            requestData(args.recipeId)
            withContext(Dispatchers.Main) {
                populateUI(recipe!!)
            }
        }
    }


    private suspend fun showError() = withContext(Dispatchers.Main) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexión.\nIntántalo de nuevo más tarde")
            .setPositiveButton("Okay, Polisha") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private suspend fun requestData(recipeId: String) {
            try {
                recipe = NetworkManagerRecipesAPI.service.getRecipeById(recipeId)
            }
            catch (error: Exception) {
                showError()
            }
    }

    private suspend fun populateUI(recipeResponse: RecipeResponse) {
        var liked: Boolean
        var stringIngredients = ""
        var stringSteps = ""
        var stringTags = ""
        recipeResponse?.let {
            binding.tvNameRecipe.text = it.name
            binding.ivImgRecipe.imageUrl(it.pictureUrl)
            binding.tvAuthor.text = it.author
            recipeResponse.ingredients.forEach {
                stringIngredients += "${it.name} ${it.amount} \n"
            }
            recipeResponse.steps.forEach {
                stringSteps += "${it.length} \n"
            }
            binding.tvIngredients.text = stringIngredients
            binding.tvSteps.text = it.steps.toString()
            binding.ibLikeDetail.setOnClickListener {
                if (isliked(liked = true)) {
                    liked = false
                } else {
                    liked = true
                }
            }
        }
    }

    private fun isliked(liked: Boolean): Boolean {
        if (liked) {
            binding.ibLikeDetail.setImageResource(R.drawable.like_detail_filled)
        } else {
            binding.ibLikeDetail.setImageResource(R.drawable.like_xavi)
        }
        return liked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //------------------------ DB

    //------------------------ NAVIGATION
}