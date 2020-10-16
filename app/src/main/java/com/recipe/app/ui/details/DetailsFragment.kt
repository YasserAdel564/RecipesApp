package com.recipe.app.ui.details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.recipe.app.R
import com.recipe.app.data.models.RecipeModel
import com.recipe.app.ui.SharedViewModel
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.details_toolbar_layout.*

class DetailsFragment : Fragment() {


    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.recipeLiveData.observe(viewLifecycleOwner, Observer { onSuccess(it) })

        back.setOnClickListener { findNavController().navigateUp() }
    }

    private fun onSuccess(model: RecipeModel) {
        Glide.with(requireActivity()).load(model.image).into(recipe_image)
        recipe_name.text = model.name
        recipe_desc.text = model.description
        recipe_head.text = model.headline
        recipe_calories.text = model.calories
        recipe_fat.text = model.fats
        recipe_difficulty.text = model.difficulty.toString()
        recipe_proteins.text = model.proteins
        recipe_carbos.text = model.carbos
        recipe_time.text = model.time

    }

}