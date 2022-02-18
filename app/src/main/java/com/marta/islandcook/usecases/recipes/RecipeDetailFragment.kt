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

    
    //------------------------ UI RELATED

    //------------------------ API REQUEST

    //------------------------ DB

    //------------------------ NAVIGATION
}