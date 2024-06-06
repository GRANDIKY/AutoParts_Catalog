package com.example.autoparts_catalog.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.autoparts_catalog.ui.elements.PartItem
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel

@Composable
fun FavouritesListScreen(innerPadding: PaddingValues, viewModel: FavouritesListViewModel) {
    val favourites by viewModel.favourites.collectAsState()

    for (favourite in favourites) {
        Log.d("FavouritesListScreen", "Favourite: $favourite")
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        LazyColumn {
            favourites.forEach { part ->
                item {
                    PartItem(part, viewModel)
                }
            }
        }
    }
}