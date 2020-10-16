package com.recipe.app.ui.recipes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.recipe.app.R
import com.recipe.app.adapters.RecipesAdapter
import com.recipe.app.data.models.RecipeModel
import com.recipe.app.ui.SharedViewModel
import com.recipe.app.utils.Injector
import com.recipe.app.utils.MyUiStates
import com.recipe.app.utils.snackBar
import com.recipe.app.utils.snackBarAction
import kotlinx.android.synthetic.main.main_toolbar_layout.*
import kotlinx.android.synthetic.main.recipes_fragment.*
import java.util.*

class RecipesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    RecipesAdapter.AdapterCallback {


    private lateinit var viewModel: SharedViewModel
    var adapter: RecipesAdapter? = null
    var alertDialogBuilder: Dialog? = null
    var sortingList: ArrayList<RecipeModel> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recipes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.uiState.observe(viewLifecycleOwner, Observer<MyUiStates?> { onResponse(it) })
        swipe_refresh.setOnRefreshListener(this)

        if (!viewModel.isOpen) {
            viewModel.isOpen = true
            viewModel.getRecipes()
        }

        search()
        sort_icon.setOnClickListener { openSortDialog() }


    }

    private fun openSortDialog() {
        val layoutInflater = LayoutInflater.from(activity)
        val view: View = layoutInflater.inflate(R.layout.sort_item_view, null)
        val alertDialogue = AlertDialog.Builder(
            context
        )
        alertDialogBuilder = alertDialogue.create()
        alertDialogBuilder!!.show()
        Objects.requireNonNull<Window>(alertDialogBuilder!!.getWindow())
            .setBackgroundDrawableResource(android.R.color.white)
        alertDialogBuilder!!.setContentView(view)
        alertDialogBuilder!!.setCancelable(true)
        val high: MaterialButton = view.findViewById(R.id.high)
        val low: MaterialButton = view.findViewById(R.id.low)

        high.setOnClickListener {
            Injector.getPreferenceHelper().sort = "high"

            requireActivity().snackBar(
                "Unfortunately the time has passed before Handling the Sort ,I saw the email today",
                recipes_root
            )
            alertDialogBuilder!!.dismiss()

        }
        low.setOnClickListener {
            Injector.getPreferenceHelper().sort = "low"

            requireActivity().snackBar(
                "Unfortunately the time has passed before Handling the Sort ,I saw the email today",
                recipes_root
            )
            alertDialogBuilder!!.dismiss()
        }
    }

    private fun search() {
        search_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter!!.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


    private fun onResponse(it: MyUiStates?) {
        when (it) {
            MyUiStates.Loading -> {
                progress.visibility = View.VISIBLE
                recycler_view.visibility = View.GONE
            }
            MyUiStates.Success -> {
                progress.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
                onSuccess()
            }

            is MyUiStates.Error -> {
                progress.visibility = View.GONE
                recycler_view.visibility = View.GONE
                requireActivity().snackBar(
                    requireActivity().getString(R.string.error_general),
                    recipes_root
                )

            }
            MyUiStates.NoConnection -> {
                recycler_view.visibility = View.GONE
                requireActivity().snackBarAction(
                    requireActivity().getString(R.string.no_internet),
                    requireActivity().getString(R.string.refresh), recipes_root
                ) {
                    viewModel.getRecipes()
                }
            }
            MyUiStates.Empty -> {
                recycler_view.visibility = View.GONE
                progress.visibility = View.GONE
                requireActivity().snackBar(
                    requireActivity().getString(R.string.no_data),
                    recipes_root
                )
            }

        }
    }

    private fun onSuccess() {
        adapter = RecipesAdapter(viewModel.recipesList!!, requireActivity(), this)
        adapter!!.notifyDataSetChanged()
        recycler_view.adapter = adapter

    }

    override fun itemClicked(model: RecipeModel) {
        viewModel.setRecipe(model)
        findNavController().navigate(R.id.from_recipes_to_details)
    }

    override fun onRefresh() {
        swipe_refresh.isRefreshing = false
        viewModel.getRecipes()
    }


}