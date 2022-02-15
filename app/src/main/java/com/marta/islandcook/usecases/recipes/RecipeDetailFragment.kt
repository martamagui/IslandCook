package com.marta.islandcook.usecases.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentHomeBinding
import com.marta.islandcook.databinding.FragmentRecipeDetailBinding
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

        }
    }

    private fun requestData(techId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.118:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: TecnologyService = retrofit.create(TecnologyService::class.java)
        service.getTechnologyById(args.techId).enqueue(object : Callback<Tecnology> {
            override fun onResponse(call: Call<Tecnology>, response: Response<Tecnology>) {
                if (response.isSuccessful) {
                    populateUi(response.body())
                } else {
                    showError("Error en la conexión")
                    val code = response.code()
                    val message = response.message()
                    Log.e("requestData", "error en la respuesta: $code <> $message")
                }
            }

            override fun onFailure(call: Call<Tecnology>, t: Throwable) {
                Log.e("requestData", "error", t)
                showError("Error en la conexión")
            }
        })
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