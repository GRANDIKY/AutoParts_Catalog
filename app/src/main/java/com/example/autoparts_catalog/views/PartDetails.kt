package com.example.autoparts_catalog.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.autoparts_catalog.viewmodels.PartDetailsViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PartDetailsScreen(viewModel: PartDetailsViewModel, partArticle: String) {
    viewModel.getPartDetail(partArticle)
    val selectedPart by viewModel.selectedPart.collectAsState()

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopStart
        ) {
        Column {
            CoilImage(imageModel = { selectedPart?.image })
            Text(text = selectedPart?.name.toString(), fontSize = 16.sp)
        }
    }
}
