package com.marta.islandcook.provider.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManagerRecipesAPI {
    private val loggin = HttpLoggingInterceptor().apply{
        this.setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    private val client =  OkHttpClient.Builder()
        .addInterceptor(loggin)
        .build()
    val service = Retrofit.Builder()
        .baseUrl("https://island-cook.herokuapp.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}