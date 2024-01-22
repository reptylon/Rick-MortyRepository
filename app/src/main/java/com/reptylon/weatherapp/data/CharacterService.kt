package com.reptylon.weatherapp.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("/api/character")
    suspend fun getCharacterResponse(): Response<CharacterResponse>

    @GET("/api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: String): Response<Character>

    companion object {
        private const val CHARACTER_URL = "https://rickandmortyapi.com/"

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(CHARACTER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val characterService: CharacterService by lazy{ retrofit.create(CharacterService::class.java)}

    }
}