package com.example.autoparts_catalog.viewmodels

import androidx.lifecycle.ViewModel
import com.example.autoparts_catalog.models.Parts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


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
        if (query.isEmpty()) {
            _parts.value = emptyList()
            return
        } else {

            val endQuery = query + "\uf8ff"

            FirebaseFirestore.getInstance().collection("parts")
                .whereGreaterThanOrEqualTo("article", query)
                .whereLessThan("article", endQuery)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val parts = querySnapshot.toObjects<Parts>()
                    _parts.value = parts
                }
                .addOnFailureListener {
                    _parts.value = emptyList()
                }
        }
    }
}