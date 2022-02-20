package com.marta.islandcook.provider.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): IslandCook_Database {
        return Room.databaseBuilder(context, IslandCook_Database::class.java, "app-database.db").build()
    }
    @Provides
    @Singleton
    fun provideRepositoryDao(db: IslandCook_Database): RecipiesDAO {
        return db.recipiesDao()
    }
}