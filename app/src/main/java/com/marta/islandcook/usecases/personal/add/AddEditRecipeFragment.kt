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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentAddEditRecipeBinding
import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.model.response.Ingredient
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.entities.Recipies
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class AddEditRecipeFragment : Fragment() {

    private val args: AddEditRecipeFragmentArgs by navArgs()

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

    private val viewModel: AddEditRecipeViewModel by viewModels()

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
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditUIState.collect { addEditUIState ->
                renderUIState(addEditUIState)
            }
        }
        viewModel.isEditedOrNot(args.recipeId)
        loadUI()
        Log.e("eEEEEEEEEEE", "hola ${args.recipeId}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //------------------------ UI
    private fun renderUIState(state: AddEditUIState) {
        if (state.isEdit) {
            binding.tvtitle.text = "Edit"
            binding.btnAddRecipe.setOnClickListener { editRecipe() }
        } else {
            binding.tvtitle.text = "Add"
            binding.btnAddRecipe.setOnClickListener {
                addRecipe()
            }
        }
        if (state.addedToAPI) {
            recipeId = state.recipeIdForDB.toString()
        }
        if (state.recipe != null) {
            setProperties(state.recipe!!)
        }
        if (state.isSuccess) {
            showMsg()
        }
    }
    private fun showMsg() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Guardado")
            .setMessage("Tu receta ha sido guardada. \uD83D\uDE0A")
            .setPositiveButton("Okay") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }
    private fun setProperties(recipeEdit: RecipeResponse) {
        binding.tietName.setText(recipeEdit.name)
        name = recipeEdit.name
        binding.tietAuthor.setText(recipeEdit.author)
        author = recipeEdit.author
        binding.tiedUrlImage.setText(recipeEdit.pictureUrl)
        urlImage = recipeEdit.pictureUrl
        binding.tiedDifficulty.setText(recipeEdit.difficulty.lowercase(Locale.getDefault()))
            .toString()
        difficulty =recipeEdit.difficulty
        recipeEdit.tags.forEach {
            Log.e("tag", "$it")
            createChip(it)
        }
        recipeEdit.ingredients.forEach{
            addIngridient(it.name, it.amount)
        }
        recipeEdit.steps.forEach {
            addStep(it)
        }
    }

    private fun removeIngredient(ingredient: Ingredient) {
        listIngredient.remove(ingredient)
        adapterIngredients.submitList(listIngredient)
        adapterIngredients.notifyDataSetChanged()
    }

    private fun removeStep(step: String) {
        listSteps.remove(step)
        adapterSteps.submitList(listSteps)
        adapterSteps.notifyDataSetChanged()
    }

    private fun loadUI() {
        setDifficulty()
        setBtns()
        setTags()
        setTabs()
        setDefaultTabView()
    }

    private fun setDefaultTabView() {
        setIngredientsTabUi()
    }

    private fun setAdapters() {
        binding.rvIngredients.adapter = adapterIngredients
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSteps.adapter = adapterSteps
        binding.rvSteps.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setTabs() {
        setAdapters()
        setIngredientsTabUi()
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    setIngredientsTabUi()
                } else if (tab?.position == 1) {
                    setStepsTabUi()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    private fun clearTxtStep(){
        binding.tiedIngredient.setText("")
        binding.tiedQuantity.setText("")
    }
    private fun addStep(step: String) {
        listSteps.add(step)
        adapterSteps.submitList(listSteps)
        adapterSteps.notifyDataSetChanged()
    }

    private fun addIngridient(ingredient: String, quantity: String) {
        listIngredient.add(Ingredient(ingredient, quantity))
        adapterIngredients.submitList(listIngredient)
        adapterSteps.notifyDataSetChanged()
    }

    private fun setBtns() {
        binding.btnAddStep.setOnClickListener {
            val step = binding.tietStep.text.toString()
            addStep(step)
            binding.tietStep.setText("")
            adapterSteps.notifyDataSetChanged()

        }
        binding.btnaAddIngredient.setOnClickListener {
            val ingredient = binding.tiedIngredient.text.toString()
            val quantity = binding.tiedQuantity.text.toString()
            addIngridient(ingredient, quantity)
            clearTxtStep()
            adapterSteps.notifyDataSetChanged()
        }
    }

    private fun createChip(text: String) {
        val chip = Chip(context)
        chip.text = text
        listTags.add(text)
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
            listTags.remove(text)
        }
    }

    private fun setTags() {
        binding.edTags.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                createChip(binding.edTags.text.toString())
                binding.edTags.setText("")
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

    //------------------------ DB REQUESTS
    private fun addRecipe() {
        getProperties()
        addRecipeDB()
    }

    private fun editRecipe() {
        getProperties()
        putRecipeApi()
        putRecipeDB()
    }

    private fun putRecipeDB() {
        viewModel.updateRecipeDB(name, urlImage, difficulty, author, args.recipeId, true)
    }

    private fun addRecipeDB() {
        val recipeDb = Recipies(
            name = name,
            author = author,
            picture_url = urlImage,
            difficulty = difficulty,
            myRecipies = true,
            recipeId = recipeId
        )
        val recipeApi =
            RecipeBody(name, listSteps, listIngredient, urlImage, difficulty, author, listTags)
        viewModel.addRecipe(recipeApi,recipeDb)
    }

    //------------------------ API REQUESTS
    private fun putRecipeApi() {
        val recipeApi =
            RecipeBody(name, listSteps, listIngredient, urlImage, difficulty, author, listTags)
        viewModel.putRecipe(args.recipeId, recipeApi!!)
    }
}




