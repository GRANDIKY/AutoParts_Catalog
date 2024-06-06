package com.example.autoparts_catalog.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autoparts_catalog.models.Parts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavouritesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _favourites = MutableLiveData<List<Parts>>(emptyList())
    val favourites: LiveData<List<Parts>> = _favourites

    private val sharedPreferences = application.getSharedPreferences("favourites", Context.MODE_PRIVATE)
    private val gson = Gson()

    init {
        loadFavourites()
    }


    fun addFavourite(part: Parts) {
        _favourites.value =
            _favourites.value?.plus(listOf(part))
        saveFavourites()
    }

    fun removeFavourite(part: Parts) {
        _favourites.value =
            _favourites.value?.minus(listOf(part).toSet())
        saveFavourites()
    }

    private fun saveFavourites() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(_favourites.value)
        editor.putString("favourites_list", json)
        editor.apply()
    }

    fun loadFavourites() {
        val json = sharedPreferences.getString("favourites_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Parts>>() {}.type
            _favourites.value = gson.fromJson(json, type)
        } else {
            _favourites.value = emptyList()
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
