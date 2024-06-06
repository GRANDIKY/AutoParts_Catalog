package com.example.autoparts_catalog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autoparts_catalog.models.Parts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarInfoViewModel(carID: String): ViewModel() {
    private val _parts = MutableStateFlow<List<Parts>>(emptyList())
    val parts: StateFlow<List<Parts>> = _parts

    fun searchPartsByID(carID: String){
        searchParts(carID = carID)
    }

    private fun searchParts(carID: String) {
        if (carID.isEmpty()) {
            _parts.value = emptyList()
            return
        } else {
            FirebaseFirestore.getInstance().collection("parts")
                .whereArrayContains("carID", carID)
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

    companion object {
        fun provideFactory(carID: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(CarInfoViewModel::class.java)) {
                    return CarInfoViewModel(carID) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}