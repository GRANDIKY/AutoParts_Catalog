package com.example.autoparts_catalog.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.autoparts_catalog.models.Cars
import com.example.autoparts_catalog.viewmodels.CarsListViewModel

@Composable
fun CarsListScreen(innerPadding: PaddingValues, viewModel: CarsListViewModel) {
    val cars = viewModel.carsState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchCarFromFirestor()
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        Button(onClick = { showDialog.value = true }) {
            Text("Добавить автомобиль")
        }

        if (showDialog.value) {
            AddCarDialog(
                cars.value,
                onDismiss = { showDialog.value = false },
                onAddCar = {
                    viewModel.saveSelectedCar(it)
                    showDialog.value = false
                }
            )
        }

        LazyColumn {
            items(cars.value) { car ->
                Text(text = "${car.make} ${car.name}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarDialog(cars: List<Cars>, onDismiss: () -> Unit, onAddCar: (Cars) -> Unit) {
    val selectedCarIndex = rememberSaveable { mutableStateOf(0) }
    val expanded = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить автомобиль")},
        text = {
               Column {
                   Text("Выбирай автомобиль:")
                   ExposedDropdownMenuBox(
                       expanded = expanded.value,
                       onExpandedChange = { expanded.value = !expanded.value}
                   ) {
                       TextField(
                           readOnly = true,
                           value = cars[selectedCarIndex.value].name,
                           onValueChange = {},
                           label = { Text("Автомобиль") },
                           trailingIcon = {
                               ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                           }
                       )
                       ExposedDropdownMenu(
                           expanded = expanded.value,
                           onDismissRequest = { expanded.value = false }
                       ) {
                           cars.forEachIndexed { index, car ->
                               DropdownMenuItem(
                                   text = { Text(text = car.name) },
                                   onClick = {
                                       selectedCarIndex.value = index
                                       expanded.value = false
                                   }
                               )
                           }
                       }
                   }
               }
        },
        confirmButton = {
            TextButton(onClick = {
                val selectedCar = cars[selectedCarIndex.value]
                onAddCar(selectedCar)
            }) {
                Text("Добавить")

            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
