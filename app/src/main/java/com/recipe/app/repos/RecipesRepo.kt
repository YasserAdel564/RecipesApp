package com.recipe.app.repos

import com.recipe.app.R
import com.recipe.app.data.models.RecipeModel
import com.recipe.app.data.storage.remote.RetrofitApiServices
import com.recipe.app.utils.DataResource
import com.recipe.app.utils.Injector
import com.recipe.app.utils.safeApiCall


class RecipesRepo(
    private val retrofitApiService: RetrofitApiServices
) {

    suspend fun recipes(): DataResource<List<RecipeModel>> {
        return safeApiCall(
            call = { recipesCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun recipesCall(): DataResource<List<RecipeModel>> {
        val response = retrofitApiService.getRecipesAsync().await()
        Injector.getAppDatabase().myDao().deleteRecipes()
        if (response.isNotEmpty())
            Injector.getAppDatabase().myDao().insertRecipes(response)
        return DataResource.Success(response)
    }


}