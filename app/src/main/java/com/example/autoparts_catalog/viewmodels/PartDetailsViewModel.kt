package com.example.autoparts_catalog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoparts_catalog.models.Cars
import com.example.autoparts_catalog.models.Parts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PartDetailsViewModel : ViewModel() {
    private val _selectedPart = MutableStateFlow<Parts?>(null)
    val selectedPart: StateFlow<Parts?> = _selectedPart

    private val _cars = MutableStateFlow<Map<String, Cars>>(emptyMap())
    val cars: StateFlow<Map<String, Cars>> = _cars

    fun getPartDetail(partArticle: String) {
        FirebaseFirestore.getInstance().collection("parts")
            .whereEqualTo("article", partArticle)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val part = querySnapshot.documents[0].toObject(Parts::class.java)
                    _selectedPart.value = part
                    part?.carID?.let { fetchCars(it) }
                } else {
                    _selectedPart.value = null
                }
            }
            .addOnFailureListener {
                _selectedPart.value = null
            }
    }

    private fun fetchCars(carIds: List<String>) {
        val carsCollection = FirebaseFirestore.getInstance().collection("cars")
        viewModelScope.launch {
            try {
                val carsMap = mutableMapOf<String, Cars?>()

                carIds.forEach { carId ->
                    val querySnapshot = carsCollection.whereEqualTo("carID", carId).get().await()
                    if (!querySnapshot.isEmpty) {
                        val car = querySnapshot.documents[0].toObject(Cars::class.java)
                        carsMap[carId] = car
                    } else {
                        carsMap[carId] = null
                    }
                }

                _cars.value = carsMap.filterValues { it != null } as Map<String, Cars>
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}
