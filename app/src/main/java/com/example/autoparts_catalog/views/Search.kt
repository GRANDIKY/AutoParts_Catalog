package com.example.autoparts_catalog.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.autoparts_catalog.activity.PartInfoActivity
import com.example.autoparts_catalog.ui.elements.PartItem
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.viewmodels.SearchViewModel


@Composable
fun SearchScreen(
    innerPadding: PaddingValues,
    viewModel: SearchViewModel,
    favouritesListViewModel: FavouritesListViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val parts by viewModel.parts.collectAsState()

    LaunchedEffect(Unit) {
        favouritesListViewModel.loadFavourites()
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        SearchTextField {
            searchQuery = it
            viewModel.onSearchQueryChanged(it)
        }

        LazyColumn {
            items(parts) { part ->
                PartItem(part, favouritesListViewModel)
            }
        }
    }
}

@Composable
fun SearchTextField(onSearch: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearch(it)
        },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.LightGray),
        placeholder = { Text(text = "Поиск...") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(searchQuery)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        singleLine = true
    )
}

fun navigateToPartInfoActivity(context: Context, partArticle: String) {
    val intent = Intent(context, PartInfoActivity::class.java)
    intent.putExtra("article", partArticle)
    context.startActivity(intent)
}
