package com.example.autoparts_catalog.activity

import CarsListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autoparts_catalog.models.CarService
import com.example.autoparts_catalog.ui.theme.AutoParts_CatalogTheme
import com.example.autoparts_catalog.viewmodels.CarsListViewModel
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.viewmodels.SearchViewModel
import com.example.autoparts_catalog.views.FavouritesListScreen
import com.example.autoparts_catalog.views.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val carService = CarService(applicationContext)

        enableEdgeToEdge()
        setContent {
            AutoParts_CatalogTheme {
                val navController = rememberNavController()
                val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
                val carListViewModel = ViewModelProvider(
                    this, CarsListViewModel.provideFactory(carService)
                )[CarsListViewModel::class.java]
                val favouritesListViewModel = ViewModelProvider(
                    this, FavouritesListViewModel.provideFactory(application)
                )[FavouritesListViewModel::class.java]

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "searchScreen") {
                        composable("searchScreen") {
                            SearchScreen(innerPadding, searchViewModel, favouritesListViewModel)
                        }
                        composable("carsScreen") {
                            CarsListScreen(innerPadding, carListViewModel, favouritesListViewModel)
                        }
                        composable("favouritesScreen") {
                            FavouritesListScreen(innerPadding, favouritesListViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                onClick = { navController.navigate("searchScreen") }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
                    Text("Поиск")
                }
            }

            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                onClick = { navController.navigate("carsScreen") }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Cars")
                    Text("Автомобили")
                }
            }

            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                onClick = { navController.navigate("favouritesScreen") }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Favourites")
                    Text("Избранное")
                }
            }
        }
    }
}