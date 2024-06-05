package com.example.autoparts_catalog.models

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Cars(
    val carID: String,
    val make: String,
    val name: String,
    val parts: List<String>,
    val year: Int
)

class CarService(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("car_prefs", Context.MODE_PRIVATE)
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchCarsFromFirestore(): List<Cars> {
        return try {
            val snapshot = firestore.collection("cars").get().await()
            snapshot.documents.mapNotNull { it.toObject(Cars::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveSelectedCar(car: Cars) {
        val carJson = Json.encodeToString(car)
        sharedPreferences.edit().putString("selected_car", carJson).apply()
    }

    fun getSelectedCar(): Cars? {
        val carJson = sharedPreferences.getString("selected_car", null)
        return carJson?.let { Json.decodeFromString(it) }
    }
}