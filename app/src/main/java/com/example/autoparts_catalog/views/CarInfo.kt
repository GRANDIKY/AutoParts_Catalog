package com.example.autoparts_catalog.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.autoparts_catalog.ui.elements.PartItem
import com.example.autoparts_catalog.viewmodels.CarInfoViewModel
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CarInfoScreen(carID: String?, viewModel: CarInfoViewModel, favouritesListViewModel: FavouritesListViewModel) {
    val parts = viewModel.parts.collectAsState()

    LaunchedEffect(Unit) {
        if (carID != null) {
            viewModel.searchPartsByID(carID)
        }
    }


    Column {
        Row {
            CoilImage(imageModel = { },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit
                ),
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1.5f)
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(text = "")
        }
        LazyColumn {
            items(parts.value) { part->
                PartItem(part = part, favouritesListViewModel = favouritesListViewModel)
            }
        }
    }
}