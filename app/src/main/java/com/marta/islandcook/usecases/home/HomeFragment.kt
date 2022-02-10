package com.marta.islandcook.usecases.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentHomeBinding
import com.marta.islandcook.usecases.common.HomeListAdapter


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    //TODO función de like y navigation
    private val adapter = HomeListAdapter({},{},false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    ///------------------------ UI RELATED
    fun setUI(){
        setAdapter()
        //TODO submitlist de API
    }
    fun setAdapter(){
        binding.rvTopRecipes.adapter = adapter
        binding.rvTopRecipes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    fun setBtn(){
        with(binding){
            //TODO añadir la navegación a todos los chips
            chipAll.setOnClickListener({})
            chipBreaksfast.setOnClickListener({})
            chipCheese.setOnClickListener({})
            chipDessert.setOnClickListener({})
            chipDinner.setOnClickListener({})
            chipEasy.setOnClickListener({})
            chipHard.setOnClickListener({})
            chipLunch.setOnClickListener({})
            chipMedium.setOnClickListener({})
            chipPasta.setOnClickListener({})
            chipPotatoes.setOnClickListener({})

        }
    }
    //------------------------ API REQUEST

    //------------------------ DB

    //------------------------ NAVIGATION
    fun navigateToRecipeList(filter: String){
        val action = HomeFragmentDirections.actionHomeFragmentToRecipeListFragment(filter)
        findNavController().navigate(action)
    }

}