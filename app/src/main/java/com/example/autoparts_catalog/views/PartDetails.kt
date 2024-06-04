package com.example.autoparts_catalog.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.autoparts_catalog.viewmodels.PartDetailsViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PartDetailsScreen(viewModel: PartDetailsViewModel, partArticle: String) {
    viewModel.getPartDetail(partArticle)
    val selectedPart by viewModel.selectedPart.collectAsState()

    Column {
        CoilImage(imageModel = { selectedPart?.image })
        Text(text = selectedPart?.name.toString())
    }
}
