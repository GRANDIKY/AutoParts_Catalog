package com.example.autoparts_catalog.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.autoparts_catalog.ui.elements.PartItem
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel

@Composable
fun FavouritesListScreen(innerPadding: PaddingValues, viewModel: FavouritesListViewModel) {
    val favourites by viewModel.favourites.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadFavourites()
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        LazyColumn {
            items(favourites) {
                PartItem(part = it, favouritesListViewModel = viewModel)
            }
        }
    }
}