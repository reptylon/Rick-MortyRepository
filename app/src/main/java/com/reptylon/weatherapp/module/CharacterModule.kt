package com.reptylon.weatherapp.module

import com.reptylon.weatherapp.MainViewModel
import com.reptylon.weatherapp.details.DetailsViewModel
import com.reptylon.weatherapp.repository.CharacterRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val characterModule = module {
    single { CharacterRepository() }
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}