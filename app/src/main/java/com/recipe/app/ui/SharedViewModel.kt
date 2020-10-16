package com.recipe.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.recipe.app.data.models.RecipeModel
import com.recipe.app.utils.DataResource
import com.recipe.app.utils.Injector
import com.recipe.app.utils.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class SharedViewModel : AppViewModel() {

    var isOpen: Boolean = false

    private var job: Job? = null
    private var _uiState = MutableLiveData<MyUiStates>()
    val uiState: LiveData<MyUiStates>
        get() = _uiState

    var recipesList: ArrayList<RecipeModel> = arrayListOf()

    fun getRecipes() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob()
        } else {
            _uiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _uiState.value = MyUiStates.Loading }

            when (val response = Injector.RecipesRepo().recipes()) {
                is DataResource.Success -> {
                    withContext(dispatcherProvider.main) {
                        if (!response.data.isNullOrEmpty()) {
                            recipesList.clear()
                            recipesList.addAll(response.data)
                            _uiState.value = MyUiStates.Success

                        } else
                            _uiState.value = MyUiStates.Empty
                    }
                }
                is DataResource.Error -> {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MyUiStates.Error(response.exception.message!!)
                    }
                }
            }
        }
    }


    //==============LiveData

    private var _recipe = MutableLiveData<RecipeModel>()
    val recipeLiveData: LiveData<RecipeModel>
        get() = _recipe

    fun setRecipe(model: RecipeModel) {
        _recipe.value = model
    }
}