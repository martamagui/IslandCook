package com.marta.islandcook.usecases.personal

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentAddEditRecipeBinding


class AddEditRecipeFragment : Fragment() {
    private var _binding: FragmentAddEditRecipeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Difficulty EditText */
        val items = listOf("Easy", "Medium", "Hard")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_difficulty, items)
        (binding.tiedDifficulty as? AutoCompleteTextView)?.setAdapter(adapter)

        /* Tags EditText + Chips */
        binding.edTags.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val chip = Chip(context)
                chip.text = binding.edTags.text
                chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
                chip.isChipIconVisible = false
                chip.isCloseIconVisible = true
                // necessary to get single selection working
                chip.isClickable = true
                chip.isCheckable = false
                binding.chipGroup.addView(chip as View)
                chip.setOnCloseIconClickListener { binding.chipGroup.removeView(chip as View) }

                return@OnKeyListener true
            }
            false
        })

    }


    //------------------------ UI RELATED

    //------------------------ API REQUEST

    //------------------------ DB

    //------------------------ NAVIGATION
}
