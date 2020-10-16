package com.recipe.app.data.storage.remote

import com.recipe.app.data.models.RecipeModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RetrofitApiServices {

    @GET("android-test/recipes.json")
    fun getRecipesAsync(): Deferred<List<RecipeModel>>
}