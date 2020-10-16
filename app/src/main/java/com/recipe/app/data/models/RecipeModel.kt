package com.recipe.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "RecipeModel")
data class RecipeModel(
    @field:Json(name = "id")
    @PrimaryKey val id: String,
    @field:Json(name = "calories")
    val calories: String?,
    @field:Json(name = "carbos")
    val carbos: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "difficulty")
    val difficulty: Int?,
    @field:Json(name = "fats")
    val fats: String?,
    @field:Json(name = "headline")
    val headline: String?,
    @field:Json(name = "image")
    val image: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "proteins")
    val proteins: String?,
    @field:Json(name = "thumb")
    val thumb: String?,
    @field:Json(name = "time")
    val time: String?
)
