package com.example.autoparts_catalog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.autoparts_catalog.viewmodels.CarInfoViewModel
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.views.CarInfoScreen

class CarInfoActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val carID = intent.getStringExtra("carID")
        val carMake = intent.getStringExtra("make")
        val carModel = intent.getStringExtra("model")
        val carImage = intent.getStringExtra("image")
        val carYear = intent.getStringExtra("year")

        setContent {
            val carInfoViewModel = ViewModelProvider(
                this, CarInfoViewModel.provideFactory()
            )[CarInfoViewModel::class.java]
            
            val favouritesListViewModel = ViewModelProvider(
                this, FavouritesListViewModel.provideFactory(application)
            )[FavouritesListViewModel::class.java]

            CarInfoScreen(carID, carMake, carModel, carImage, carYear, carInfoViewModel, favouritesListViewModel)
        }
    }
}