package com.marta.islandcook.di

import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideInjectedClassOne(): NetworkManagerRecipesAPI{
        return NetworkManagerRecipesAPI
    }
}