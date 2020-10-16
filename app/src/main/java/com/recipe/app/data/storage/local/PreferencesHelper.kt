package com.recipe.app.data.storage.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(private val mCtx: Context) {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        const val SHARED_NAME = "RecipeApp"
        private const val Sort = "SORT"
    }
    init {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }
    var sort = sharedPreferences.getString(Sort, "all")
        set(value) = sharedPreferences.edit().putString(Sort, value).apply()

}