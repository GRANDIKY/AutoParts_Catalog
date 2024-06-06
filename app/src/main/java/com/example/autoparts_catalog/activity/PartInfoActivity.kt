package com.example.autoparts_catalog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.viewmodels.PartDetailsViewModel
import com.example.autoparts_catalog.views.PartDetailsScreen

class PartInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val partArticle = intent.getStringExtra("article")

        setContent {
            val partDetailsViewModel = ViewModelProvider(this)[PartDetailsViewModel::class.java]
            val favouritesListViewModel = ViewModelProvider(
                this, FavouritesListViewModel.provideFactory(application)
            )[FavouritesListViewModel::class.java]

            PartDetailsScreen(partDetailsViewModel, partArticle.toString(), favouritesListViewModel)
        }
    }
}