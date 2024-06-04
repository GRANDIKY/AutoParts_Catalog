package com.example.autoparts_catalog.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.autoparts_catalog.viewmodels.PartDetailsViewModel
import com.example.autoparts_catalog.views.PartDetailsScreen

class PartInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("PartInfoActivity", "Activity created")

        val partArticle = intent.getStringExtra("article")

        setContent {
            val partDetailsViewModel = ViewModelProvider(this)[PartDetailsViewModel::class.java]
            PartDetailsScreen(partDetailsViewModel, partArticle.toString())
        }
    }
}