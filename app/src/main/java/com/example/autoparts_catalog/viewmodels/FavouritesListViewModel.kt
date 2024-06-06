package com.example.autoparts_catalog.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.autoparts_catalog.models.Parts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavouritesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _favourites = MutableStateFlow<List<Parts>>(emptyList())
    val favourites: StateFlow<List<Parts>> = _favourites

    private val sharedPreferences = application.getSharedPreferences("favourites", Context.MODE_PRIVATE)
    private val gson = Gson()

    init {
        loadFavourites()
    }

    fun addFavourite(part: Parts) {
        _favourites.value = _favourites.value + listOf(part) // Обновление состояния с добавлением нового элемента
        saveFavourites()
        Log.d("FavouritesListViewModel", "Added favourite: $part")
    }

    fun removeFavourite(part: Parts) {
        _favourites.value = _favourites.value - listOf(part) // Обновление состояния с удалением элемента
        saveFavourites()
        Log.d("FavouritesListViewModel", "Removed favourite: $part")
    }


    fun isFavourite(part: Parts?): Boolean {
        return part != null && _favourites.value.contains(part)
    }

    private fun saveFavourites() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(_favourites.value)
        editor.putString("favourites_list", json)
        editor.apply()
    }

    private fun loadFavourites() {
        val json = sharedPreferences.getString("favourites_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Parts>>() {}.type
            _favourites.value = gson.fromJson(json, type)
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(FavouritesListViewModel::class.java)) {
                        "UNCHECKED_CAST"
                        return FavouritesListViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
