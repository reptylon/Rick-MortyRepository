package com.reptylon.weatherapp

import com.reptylon.weatherapp.data.Character

//class UiState(
//    val characters: List<Character>? = null,
//    val isLoading: Boolean = false,
//    val error: String? = null
//)


data class UiState<T>(
    val characters: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)