package com.example.autoparts_catalog.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.viewmodels.PartDetailsViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PartDetailsScreen(
    viewModel: PartDetailsViewModel,
    partArticle: String,
    favouritesListViewModel: FavouritesListViewModel
) {
    viewModel.getPartDetail(partArticle)
    val selectedPart by viewModel.selectedPart.collectAsState()
    val carsMap by viewModel.cars.collectAsState()
    val isFavourite = favouritesListViewModel.isFavourite(selectedPart)

    Log.d("PartDetailsScreen", "selectedPart: $selectedPart")
    Log.d("PartDetailsScreen", "isFavourite: $isFavourite")

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            CoilImage(
                imageModel = { selectedPart?.image },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedPart?.name.orEmpty(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    selectedPart?.let { part ->
                        if (isFavourite) {
                            favouritesListViewModel.removeFavourite(part)
                        } else {
                            favouritesListViewModel.addFavourite(part)
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavourite) Color.Red else Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Описание", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = selectedPart?.description.orEmpty(), fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Характеристики", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Подходит для автомобилей:", fontSize = 14.sp, color = Color.Gray)
            selectedPart?.carID?.forEach { carId ->
                val car = carsMap[carId]
                if (car != null) {
                    Text(text = "${car.make} ${car.name}", fontSize = 14.sp, color = Color.Black)
                }
            }
        }
    }
}
