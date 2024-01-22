package com.reptylon.weatherapp.data

data class CharacterResponse(
    val count: Int,
    val results: List<Character>
)

data class Character(
    val id: String,
    val name: String,
    val species: String,
    val gender: String,
    val image: String,
    val origin: Origin
)

data class Origin(
    val name: String,
    val url: String
)