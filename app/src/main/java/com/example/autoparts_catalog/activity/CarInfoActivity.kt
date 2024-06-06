package com.example.autoparts_catalog.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.autoparts_catalog.viewmodels.CarInfoViewModel
import com.example.autoparts_catalog.viewmodels.CarsListViewModel
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.views.CarInfoScreen

class CarInfoActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val carID = intent.getStringExtra("carID")
        Log.d("INTENT", "GET INTENT ${carID}")

        setContent {
            val carInfoViewModel = ViewModelProvider(
                this, CarInfoViewModel.provideFactory(carID.toString())
            )[CarInfoViewModel::class.java]
            
            val favouritesListViewModel = ViewModelProvider(
                this, FavouritesListViewModel.provideFactory(application)
            )[FavouritesListViewModel::class.java]

            CarInfoScreen(carID, carInfoViewModel, favouritesListViewModel)
        }
    }
}