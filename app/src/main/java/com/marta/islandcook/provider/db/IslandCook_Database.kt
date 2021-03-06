package com.marta.islandcook.provider.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marta.islandcook.provider.db.entities.Ingredients
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.provider.db.entities.Steps
import com.marta.islandcook.provider.db.entities.Tags


@Database(entities = [Recipies::class, Ingredients::class, Steps::class, Tags::class], version = 1)
abstract class IslandCook_Database: RoomDatabase() {
    abstract fun recipiesDao(): RecipiesDAO

    companion object {
        @Volatile
        private var INSTANCE: IslandCook_Database? = null

        fun getInstance(context: Context): IslandCook_Database = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, IslandCook_Database::class.java, "task.db")
                .build()
    }
}