package com.reptylon.weatherapp


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reptylon.weatherapp.data.CharacterResponse
import com.reptylon.weatherapp.data.CharacterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.reptylon.weatherapp.data.Character
import com.reptylon.weatherapp.repository.CharacterRepository
import retrofit2.Response

class MainViewModel(private val characterRepository: CharacterRepository) : ViewModel() {
//    private val characterService = CharacterService.retrofit.create(CharacterService::class.java)



    private suspend fun getCharacterResponse(): Response<CharacterResponse> = characterRepository.getCharacterResponse()

    // private val mutableCharacterData = MutableLiveData<UiState>()
    // val immutableCharacterData: LiveData<UiState> = mutableCharacterData
    private val mutableCharacterData = MutableLiveData<UiState<List<Character>>>()
    val immutableCharacterData: LiveData<UiState<List<Character>>> = mutableCharacterData

    fun getData() {

        mutableCharacterData.postValue(UiState(isLoading = true))

        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val characters = getCharacterResponse().body()?.results
//                mutableCharacterData.postValue(UiState(characters = characters))

                val request = getCharacterResponse()
                Log.d("Main", "starships response: ${request.code()}, ${request.isSuccessful}")
                if (request.isSuccessful) {
                    val characters = request.body()?.results
                    mutableCharacterData.postValue(UiState(characters = characters))
                } else {
                    mutableCharacterData.postValue(UiState(error = "Fail, code: ${request.code()}"))
                }

            } catch (e:Exception){
                mutableCharacterData.postValue(UiState(error = e.message))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }

    val filterQuery = MutableLiveData("")

    fun updateFilterQuery(text: String){
        filterQuery.postValue(text)
    }
}