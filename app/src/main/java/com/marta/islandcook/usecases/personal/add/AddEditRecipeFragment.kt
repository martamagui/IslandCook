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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentAddEditRecipeBinding
import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.model.response.Ingredient
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddEditRecipeFragment : Fragment() {

    private val args: AddEditRecipeFragmentArgs by navArgs()
    private val idRecipeToEdit: String? = args.recipeId

    private var _binding: FragmentAddEditRecipeBinding? = null
    private val binding get() = _binding!!

    private var recipeId = ""
    private var name = ""
    private var author = ""
    private var urlImage = ""
    private var difficulty = ""
    private var listIngredient: MutableList<Ingredient> = mutableListOf()
    private var listSteps: MutableList<String> = mutableListOf()
    private var listTags: MutableList<String> = mutableListOf()

    private val adapterIngredients = ListAdapterIngredients(listIngredient, ::removeIngredient)
    private val adapterSteps = ListAdapterSteps(listSteps, ::removeStep)

    private var recipeApi : RecipeBody? = null
    private var recipeDb : Recipies? = null

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
        loadUI()
    }

    private suspend fun addRecipe() {
        getProperties()
        addRecipeApi()
        addRecipeDB()
    }

    private suspend fun editRecipe(recipe : RecipeResponse){
        setProperties(recipe)
        putRecipeApi()
        putRecipeDB()
    }

    private suspend fun putRecipeDB() {
        val recipeToEditDB =  IslandCook_Database.getInstance(requireContext()).recipiesDao().findRecipiesById(recipeId)
        IslandCook_Database.getInstance(requireContext()).recipiesDao().updateRecipies(name, urlImage,difficulty,author,recipeToEditDB.recipeId,true)
    }

    private suspend fun putRecipeApi() {
        recipeApi = RecipeBody(name, listSteps, listIngredient, urlImage, difficulty, author, listTags)
        recipeId =
            idRecipeToEdit?.let { NetworkManagerRecipesAPI.service.editRecipe(it,recipeApi!!).id }.toString()
    }

    private fun setProperties(recipeEdit: RecipeResponse) {
        binding.tietName.setText(recipeEdit.name)
        binding.tietAuthor.setText(recipeEdit.author)
        binding.tiedUrlImage.setText(recipeEdit.pictureUrl)
        binding.tiedDifficulty.setText(recipeEdit.difficulty.lowercase(Locale.getDefault())).toString()
        val chip = Chip(context)
        chip.text = binding.edTags.text
        listTags = recipeEdit.tags.toMutableList()
        chip.chipIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
        chip.isChipIconVisible = false
        chip.isCloseIconVisible = true
        chip.isClickable = true
        chip.isCheckable = false
        binding.chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(chip as View)
            listTags.removeAt(listTags.indexOf(binding.edTags.text.toString()))
        }
        listIngredient = recipeEdit.ingredients.toMutableList()
        adapterIngredients.submitList(listIngredient)
        listSteps = recipeEdit.steps.toMutableList()
        adapterSteps.submitList(listSteps)
    }

    private suspend fun addRecipeApi() {
        recipeApi = RecipeBody(name, listSteps, listIngredient, urlImage, difficulty, author, listTags)
        recipeId = NetworkManagerRecipesAPI.service.addRecipe(recipeApi!!).id
    }
    private suspend fun addRecipeDB() {
        recipeDb = Recipies(
            name = name,
            author = author,
            picture_url = urlImage,
            difficulty = difficulty,
            myRecipies = true,
            recipeId = recipeId
        )
        IslandCook_Database.getInstance(requireContext()).recipiesDao().insertRecipies(recipeDb!!)
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

    private fun loadUI() {
        setDifficulty()
        setTags()
        setTabs()
        setDefaultTabView()
    }

    private fun setDefaultTabView() {
        setIngredientsTabUi()
    }

    private fun setTabs() {
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    setIngredientsTabUi()
                    binding.rvIngredients.adapter = adapterIngredients
                    binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
                    addIngredient()
                    binding.btnAddRecipe.setOnClickListener {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            addRecipe()
                        }
                    }

                } else if (tab?.position == 1) {
                    setStepsTabUi()
                    binding.rvIngredients.adapter = adapterSteps
                    binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
                    addStep()
                    binding.btnAddRecipe.setOnClickListener {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            addRecipe()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            private fun addStep() {
                binding.btnAddStep.setOnClickListener {
                    val step = binding.tietStep.text.toString()
                    listSteps.add(step)
                    adapterSteps.submitList(listSteps)
                }
            }

            private fun addIngredient() {
                binding.btnaAddIngredient.setOnClickListener {
                    val ingredient = binding.tiedIngredient.text.toString()
                    val quantity = binding.tiedQuantity.text.toString()
                    listIngredient.add(Ingredient(ingredient, quantity))
                    adapterIngredients.submitList(listIngredient)
                }
            }
        })
    }


    private fun setTags() {
        binding.edTags.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
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
    }

    private fun getProperties() {
        name = binding.tietName.text.toString()
        author = binding.tietAuthor.text.toString()
        urlImage = binding.tiedUrlImage.text.toString()
        difficulty = binding.tiedDifficulty.text.toString().lowercase(Locale.getDefault())
    }

    private fun setDifficulty() {
        val items = listOf("Easy", "Medium", "Hard")
        val adapterDifficulty = ArrayAdapter(requireContext(), R.layout.list_difficulty, items)
        (binding.tiedDifficulty as? AutoCompleteTextView)?.setAdapter(adapterDifficulty)
    }
    private fun setStepsTabUi() {
        binding.edStep.isEnabled = true
        binding.edStep.visibility = View.VISIBLE
        binding.edIngredient.isEnabled = false
        binding.edIngredient.visibility = View.INVISIBLE
        binding.edQuantity.isEnabled = false
        binding.edQuantity.visibility = View.INVISIBLE
        binding.btnaAddIngredient.isEnabled = false
        binding.btnaAddIngredient.visibility = View.INVISIBLE
        binding.btnAddStep.isEnabled = true
        binding.btnAddStep.visibility = View.VISIBLE
        binding.rvIngredients.isEnabled = false
        binding.rvIngredients.visibility = View.INVISIBLE
        binding.rvSteps.isEnabled = true
        binding.rvSteps.visibility = View.VISIBLE
    }

    private fun setIngredientsTabUi() {
        binding.edIngredient.isEnabled = true
        binding.edIngredient.visibility = View.VISIBLE
        binding.edStep.isEnabled = false
        binding.edStep.visibility = View.INVISIBLE
        binding.edQuantity.isEnabled = true
        binding.edQuantity.visibility = View.VISIBLE
        binding.btnaAddIngredient.isEnabled = true
        binding.btnaAddIngredient.visibility = View.VISIBLE
        binding.btnAddStep.isEnabled = false
        binding.btnAddStep.visibility = View.INVISIBLE
        binding.rvIngredients.isEnabled = true
        binding.rvIngredients.visibility = View.VISIBLE
        binding.rvSteps.isEnabled = false
        binding.rvSteps.visibility = View.INVISIBLE
    }
}




