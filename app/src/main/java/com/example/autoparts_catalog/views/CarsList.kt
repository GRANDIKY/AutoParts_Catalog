package com.example.autoparts_catalog.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autoparts_catalog.viewmodels.CarsListViewModel

@Composable
fun CarsListScreen(innerPudding: PaddingValues, viewModel: CarsListViewModel) {
    val cars = null

    Column {
        Button(onClick = { }) {

        }
        Spacer(modifier = Modifier
            .height(10.dp)
            .aspectRatio(1.5f)
        )
        Text(text = "Список добавленных автомобилей:")
        Spacer(modifier = Modifier
            .height(10.dp)
            .aspectRatio(1.5f)
        )
        LazyColumn{}
    }
}
