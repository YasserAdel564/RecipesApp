package com.recipe.app.data.storage.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.recipe.app.data.models.RecipeModel

@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(item: List<RecipeModel>)

    @Query("SELECT * FROM RecipeModel ORDER BY calories")
    fun getSortingRecipes(): LiveData<List<RecipeModel>>

    @Query("DELETE FROM RecipeModel")
    fun deleteRecipes()



}