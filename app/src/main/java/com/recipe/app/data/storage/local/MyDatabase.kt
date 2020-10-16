package com.recipe.app.data.storage.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.recipe.app.data.models.RecipeModel

@Database(
    entities = [RecipeModel::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun myDao(): MyDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "mydatabasea.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}