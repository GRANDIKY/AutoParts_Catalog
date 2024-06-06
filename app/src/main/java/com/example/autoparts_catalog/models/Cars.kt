package com.example.autoparts_catalog.models

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Cars(
    val carID: String = "",
    val image: String = "",
    val make: String = "",
    val name: String = "",
    val parts: List<String> = emptyList(),
    val year: Int = 0
)

class CarService(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("car_prefs", Context.MODE_PRIVATE)
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "CarService"
        private const val SELECTED_CAR_KEY = "selected_car"
        private const val SAVED_CARS_KEY = "saved_cars"
        private const val MIGRATION_DONE_KEY = "migration_done"
    }

    init {
        migrateDataIfNeeded()
    }

    private fun migrateDataIfNeeded() {
        val migrationDone = sharedPreferences.getBoolean(MIGRATION_DONE_KEY, false)
        if (!migrationDone) {
            migrateData()
            sharedPreferences.edit().putBoolean(MIGRATION_DONE_KEY, true).apply()
        }
    }

    private fun migrateData() {
        // Check if data exists as a string
        val savedCarsString = sharedPreferences.getString(SAVED_CARS_KEY, null)
        if (savedCarsString != null) {
            try {
                val savedCars: MutableList<Cars> = Json.decodeFromString(savedCarsString)
                // Save the list as a string
                sharedPreferences.edit().putString(SAVED_CARS_KEY, Json.encodeToString(savedCars)).apply()
                Log.d(TAG, "Data migrated from String to List")
            } catch (e: Exception) {
                Log.e(TAG, "Error migrating data", e)
            }
        } else {
            // Check if data exists as a string set
            val savedCarsSet = sharedPreferences.getStringSet(SAVED_CARS_KEY, null)
            if (savedCarsSet != null) {
                try {
                    val savedCars = savedCarsSet.map { Json.decodeFromString<Cars>(it) }.toMutableList()
                    // Save the list as a string
                    sharedPreferences.edit().putString(SAVED_CARS_KEY, Json.encodeToString(savedCars)).apply()
                    Log.d(TAG, "Data migrated from Set to List")
                } catch (e: Exception) {
                    Log.e(TAG, "Error migrating data", e)
                }
            }
        }
    }

    suspend fun fetchCarsFromFirestore(): List<Cars> {
        return try {
            Log.d(TAG, "Fetching cars from Firestore")
            val snapshot = firestore.collection("cars").get().await()
            val cars = snapshot.documents.mapNotNull {
                it.toObject(Cars::class.java)?.also { car ->
                    Log.d(TAG, "Fetched car: $car")
                }
            }
            Log.d(TAG, "Successfully fetched ${cars.size} cars from Firestore")
            cars
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching cars from Firestore", e)
            emptyList()
        }
    }

    fun saveSelectedCar(car: Cars) {
        val carJson = Json.encodeToString(car)
        sharedPreferences.edit().putString(SELECTED_CAR_KEY, carJson).apply()
        Log.d(TAG, "Saved selected car: $carJson")
        addCarToSavedList(car)
    }

    private fun addCarToSavedList(car: Cars) {
        val savedCarsJson = sharedPreferences.getString(SAVED_CARS_KEY, "[]")
        val savedCars = Json.decodeFromString<MutableList<Cars>>(savedCarsJson!!)
        savedCars.add(car)
        sharedPreferences.edit().putString(SAVED_CARS_KEY, Json.encodeToString(savedCars)).apply()
        Log.d(TAG, "Updated saved cars list: $savedCars")
    }

    fun getSavedCars(): List<Cars> {
        val savedCarsJson = sharedPreferences.getString(SAVED_CARS_KEY, "[]")
        return Json.decodeFromString(savedCarsJson!!)
    }

    fun removeSavedCar(car: Cars) {
        val savedCarsJson = sharedPreferences.getString(SAVED_CARS_KEY, "[]")
        val savedCars = Json.decodeFromString<MutableList<Cars>>(savedCarsJson!!)
        savedCars.remove(car)
        sharedPreferences.edit().putString(SAVED_CARS_KEY, Json.encodeToString(savedCars)).apply()
        Log.d(TAG, "Removed car: $car, Updated saved cars list: $savedCars")
    }

    fun getSelectedCar(): Cars? {
        val carJson = sharedPreferences.getString(SELECTED_CAR_KEY, null)
        return if (carJson != null) {
            val car = Json.decodeFromString<Cars>(carJson)
            Log.d(TAG, "Retrieved selected car: $carJson")
            car
        } else {
            Log.d(TAG, "No selected car found in SharedPreferences")
            null
        }
    }
}
