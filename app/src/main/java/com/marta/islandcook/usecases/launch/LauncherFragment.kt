package com.marta.islandcook.usecases.launch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentLauncherBinding


class LauncherFragment : Fragment() {
    private var _binding: FragmentLauncherBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLauncherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNavigateToHome.setOnClickListener {
            navigationToHome()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //------------------------ UI RELATED

    //------------------------ API REQUEST

    //------------------------ DB

    //------------------------ NAVIGATION
    fun navigationToHome(){
        val action = LauncherFragmentDirections.actionLauncherFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}