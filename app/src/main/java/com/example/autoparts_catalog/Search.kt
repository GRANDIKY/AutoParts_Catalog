package com.example.autoparts_catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable


@Composable
fun SearchScreen(innerPudding: PaddingValues, viewModel: SearchViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val parts = viewModel.parts.collectAsState()

    Column(modifier = Modifier.padding(innerPudding)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .background(Color.LightGray),
            placeholder = { Text(text = "Поиск...") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") }
        )

        LazyColumn {
            items(parts.value) { part ->
                PartItem(part)
            }
        }
    }
}

class SearchViewModel: ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _parts = MutableStateFlow<List<Parts>>(emptyList())
    val parts: StateFlow<List<Parts>> = _parts

    fun onSearchQueryChanged(query: String){
        _searchQuery.value = query
        searchParts(query)
    }

    private fun searchParts(query: String) {
        FirebaseFirestore.getInstance().collection("parts")
            .whereEqualTo("article", query)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val parts = querySnapshot.toObjects<Parts>()
                _parts.value = parts
            }
    }
}

@Composable
fun PartItem(part: Parts) {
    Text("${part.name} - \$${part.article}")
}

@Composable
fun SearchTextField() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.LightGray),
        placeholder = { Text(text = "Поиск...") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") }
    )
}


@Serializable
data class Parts(
    val article: String,
    val carID: List<String>,
    val description: String,
    val name: String
)

@Serializable
data class Cars(
    val carID: String,
    val make: String,
    val name: String,
    val parts: List<String>,
    val year: Int
)
