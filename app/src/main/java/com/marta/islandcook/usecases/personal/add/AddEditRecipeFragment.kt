package com.marta.islandcook.usecases.personal.add

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentAddEditRecipeBinding
import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.model.response.Ingredient
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddEditRecipeFragment : Fragment() {

    private var _binding: FragmentAddEditRecipeBinding? = null
    private val binding get() = _binding!!

    private val listIngredient: MutableList<Ingredient> = mutableListOf()
    private val listSteps: MutableList<String> = mutableListOf()
    private val listTags: MutableList<String> = mutableListOf()

    private val adapterIngredients = ListAdapterIngredients(listIngredient, ::removeIngredient)
    private val adapterSteps = ListAdapterSteps(listSteps, ::removeStep)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditRecipeBinding.inflate(inflater, container, false)
        return binding.root
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
                listTags.add(binding.edTags.text.toString())
                chip.chipIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
                chip.isChipIconVisible = false
                chip.isCloseIconVisible = true
                // necessary to get single selection working
                chip.isClickable = true
                chip.isCheckable = false
                binding.chipGroup.addView(chip as View)
                chip.setOnCloseIconClickListener {
                    binding.chipGroup.removeView(chip as View)
                    listTags.removeAt(listTags.indexOf(binding.edTags.text.toString()))
                }

                return@OnKeyListener true
            }
            false
        })
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.edIngredient.isEnabled = true
                    binding.edIngredient.visibility = View.VISIBLE
                    binding.edStep.isEnabled = false
                    binding.edStep.visibility = View.INVISIBLE

                    /* Add Ingredient on Recycler view */
                    binding.rvIngredients.adapter = adapterIngredients
                    binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())

                    binding.btnaAddIngredient.setOnClickListener {
                        val ingredient = binding.tiedIngredient.text.toString()
                        val quantity = binding.tiedQuantity.text.toString()
                        listIngredient.add(Ingredient(ingredient, quantity))
                        adapterIngredients.submitList(listIngredient)

                    }

                    binding.btnaAddStep.setOnClickListener {
                        val step = binding.tietStep.text.toString()
                        listSteps.add(step)
                        adapterSteps.submitList(listSteps)
                    }

                    binding.btnAddRecipe.setOnClickListener {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            addRecipe()
                        }
                    }


                } else if (tab?.position == 1) {
                    binding.edStep.isEnabled = true
                    binding.edStep.visibility = View.VISIBLE
                    binding.edIngredient.isEnabled = false
                    binding.edIngredient.visibility = View.INVISIBLE

                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

    }

    private suspend fun addRecipe() {

        /* Recipe Properties */
        val name = binding.tietName.text.toString()
        val author = binding.tietAuthor.text.toString()
        val urlImage = binding.tiedUrlImage.text.toString()
        val difficulty = binding.tiedDifficulty.text.toString()
        val tagsList = binding.edTags.text.toString()

        /* Recipe Api */
        val recipeApi = RecipeBody(
            name = name,
            author = author,
            pictureUrl = urlImage,
            difficulty = difficulty,
            ingredients = listIngredient,
            steps = listSteps,
            tags = listTags
        )
        val recipeId = NetworkManagerRecipesAPI.service.addRecipe(recipeApi).id

        /* Recipe DB */
        val recipeDb = Recipies(
            name = name,
            author = author,
            picture_url = urlImage,
            difficulty = difficulty,
            myRecipies = true,
            recipeId = recipeId
        )
        IslandCook_Database.getInstance(requireContext()).recipiesDao().insertRecipies(recipeDb)
    }

    private fun removeIngredient(ingredient: Ingredient) {
        listIngredient.removeAt(listIngredient.indexOf(ingredient))
        adapterIngredients.submitList(listIngredient)
    }

    private fun removeStep(step: String) {
        listSteps.remove(step)
        adapterSteps.submitList(listSteps)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




