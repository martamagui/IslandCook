package com.marta.islandcook.usecases.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentHomeBinding
import com.marta.islandcook.databinding.FragmentRecipeDetailBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.utils.imageUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RecipeDetailFragment : Fragment() {
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
        args.recipeId.let {
            requestData(it)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun requestData(recipeId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.118:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: NetworkService = retrofit.create(NetworkService::class.java)
        service.getRecipeById(args.recipeId).enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { populateUI(it) }
                } else {
                    showError("Error en la conexión")
                    val code = response.code()
                    val message = response.message()
                    Log.e("requestData", "error en la respuesta: $code <> $message")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e("requestData", "error", t)
                showError("Error en la conexión")
            }
        })
    }

    private fun populateUI(recipeResponse: RecipeResponse) {
        recipeResponse?.let {
            binding.tvNameRecipe.text = it.name
            binding.ivImgRecipe.imageUrl(it.pictureUrl)
            binding.tvAuthor.text = it.author
            binding.tvIngredients.text = it.ingredients.toString()
            binding.tvSteps.text = it.steps.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //TODO _binding = null
    }
    //------------------------ UI RELATED

    //------------------------ API REQUEST

    //------------------------ DB

    //------------------------ NAVIGATION
}