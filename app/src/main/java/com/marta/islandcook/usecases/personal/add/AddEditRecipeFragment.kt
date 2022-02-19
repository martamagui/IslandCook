package com.marta.islandcook.usecases.personal.add

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentAddEditRecipeBinding
import com.marta.islandcook.model.response.Ingredient


class AddEditRecipeFragment : Fragment() {

    private var _binding: FragmentAddEditRecipeBinding? = null
    private val binding get() = _binding!!
    private val listIngredient: MutableList<IngredientObj> = mutableListOf()
    private val adapterIngredients = ListAdapter(listIngredient) { removeIngredient(IngredientObj()) }

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
        val adapterDifficulty = ArrayAdapter(requireContext(), R.layout.list_difficulty, items)
        (binding.tiedDifficulty as? AutoCompleteTextView)?.setAdapter(adapterDifficulty)

        /* Tags EditText + Chips */
        binding.edTags.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val chip = Chip(context)
                chip.text = binding.edTags.text
                chip.chipIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
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


        /* Add IngredientObj on Recycler view */
        binding.rv.adapter = adapterIngredients
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.btnaAddIngredient.setOnClickListener {
            val ingredient = binding.tiedIngredient.text.toString()
            val quantity = binding.tiedQuantity.text.toString()
            listIngredient.add(IngredientObj(ingredient, quantity))
            adapterIngredients.notifyDataSetChanged()

        }
    }
    private fun removeIngredient(ingredient: IngredientObj){
        Log.d("indexAddFragment",listIngredient.indexOf(ingredient).toString())
        listIngredient.removeAt(listIngredient.indexOf(ingredient))
        adapterIngredients.notifyItemRemoved(listIngredient.indexOf(ingredient)+1)
    }

    override fun onResume() {
        super.onResume()
        binding.rv.adapter?.notifyDataSetChanged()
    }

}




