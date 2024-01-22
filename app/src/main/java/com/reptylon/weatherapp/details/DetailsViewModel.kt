package com.reptylon.weatherapp.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reptylon.weatherapp.UiState
import com.reptylon.weatherapp.data.CharacterService
import com.reptylon.weatherapp.data.Character
import com.reptylon.weatherapp.data.Origin
import com.reptylon.weatherapp.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailsViewModel(private val characterRepository: CharacterRepository) : ViewModel() {

//    private val characterService = CharacterService.retrofit.create(CharacterService::class.java)

    private val _characterDetail = MutableLiveData<UiState<Character?>>()
    val characterDetail: LiveData<UiState<Character?>> get() = _characterDetail

    suspend fun loadCharacterDetail(id: String) {
        _characterDetail.value = UiState(isLoading = true)

        try {
            withContext(Dispatchers.IO) {
                val detailsResponse = characterRepository.getCharacterById(id).body()
                val details = Character(
                    id = detailsResponse?.id ?:"",
                    name = detailsResponse?.name ?:"",
                    species = detailsResponse?.species ?:"",
                    image = detailsResponse?.image ?:"",
                    gender = detailsResponse?.gender ?:"",
                    origin = detailsResponse?.origin ?: Origin(name = "", url = ""),
                )
                _characterDetail.postValue(UiState(characters = details, isLoading = false))
            }

        } catch (e: Exception) {
            _characterDetail.value = UiState(error = "Operacja nie powiodła się", isLoading = false)
        }
    }
}