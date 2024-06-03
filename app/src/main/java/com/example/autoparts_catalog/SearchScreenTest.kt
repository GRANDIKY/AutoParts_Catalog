package com.example.autoparts_catalog

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autoparts_catalog.activity.BottomNavigationBar
import com.example.autoparts_catalog.views.SearchScreen
import com.example.autoparts_catalog.viewmodels.SearchViewModel


val searchViewModel = SearchViewModel()

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "searchScreen") {
            composable("searchScreen") { SearchScreen(innerPadding, searchViewModel) }
        }
    }
}