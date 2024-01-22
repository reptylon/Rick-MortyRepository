package com.reptylon.weatherapp.repository

import com.reptylon.weatherapp.data.Character
import com.reptylon.weatherapp.data.CharacterResponse
import com.reptylon.weatherapp.data.CharacterService
import retrofit2.Response

class CharacterRepository {
    suspend fun getCharacterResponse(): Response<CharacterResponse> =
        CharacterService.characterService.getCharacterResponse()

    suspend fun getCharacterById(id: String): Response<Character> =
        CharacterService.characterService.getCharacterById(id)
}