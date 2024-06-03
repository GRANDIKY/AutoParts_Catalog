package com.example.autoparts_catalog.viewmodels

import androidx.lifecycle.ViewModel
import com.example.autoparts_catalog.models.Parts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PartDetailsViewModel: ViewModel() {
    private val _selectedPart = MutableStateFlow<Parts?>(null)
    val selectedPart: StateFlow<Parts?> = _selectedPart
    fun getPartDetail(partArticle: String) {
        FirebaseFirestore.getInstance().collection("parts")
            .whereEqualTo("article", partArticle)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val part = querySnapshot.documents[0].toObject(Parts::class.java)
                    _selectedPart.value = part
                } else {
                    _selectedPart.value = null
                }
            }
            .addOnFailureListener {
                _selectedPart.value = null
            }
    }
}