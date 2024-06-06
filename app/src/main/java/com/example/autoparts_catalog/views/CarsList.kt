
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.autoparts_catalog.activity.CarInfoActivity
import com.example.autoparts_catalog.models.Cars
import com.example.autoparts_catalog.viewmodels.CarsListViewModel
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CarsListScreen(
    innerPadding: PaddingValues,
    viewModel: CarsListViewModel,
    favouritesListViewModel: FavouritesListViewModel
) {
    val context = LocalContext.current
    val savedCars = viewModel.savedCarsState.collectAsState()
    val allCars = viewModel.carsState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchCarsFromFirestore()
        favouritesListViewModel.loadFavourites()
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        Button(onClick = {
            if (allCars.value.isNotEmpty()) {
                showDialog.value = true
            }
        }) {
            Text("Добавить автомобиль")
        }

        if (showDialog.value) {
            AddCarDialog(
                cars = allCars.value,
                onDismiss = { showDialog.value = false },
                onAddCar = {
                    viewModel.saveSelectedCar(it)
                    showDialog.value = false
                }
            )
        }

        LazyColumn {
            items(savedCars.value) { car ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .clickable { navigateToCarInfoActivity(context, car.carID) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoilImage(imageModel = { car.image },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Fit
                        ),
                        modifier = Modifier
                            .size(150.dp)
                            .aspectRatio(1.5f)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Text(text = "${car.make} ${car.name}")
                    IconButton(onClick = { viewModel.removeSavedCar(car) }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Удалить")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarDialog(cars: List<Cars>, onDismiss: () -> Unit, onAddCar: (Cars) -> Unit) {
    val selectedCarIndex = rememberSaveable { mutableStateOf(-1) }
    val expanded = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить автомобиль") },
        text = {
            Column {
                Text("Выбирай автомобиль:")
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value }
                ) {
                    TextField(
                        readOnly = true,
                        value = if (selectedCarIndex.value >= 0) cars[selectedCarIndex.value].name else "",
                        onValueChange = {},
                        label = { Text("Автомобиль") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        modifier = Modifier.menuAnchor()
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
            if (selectedCarIndex.value >= 0) {
                TextButton(onClick = {
                    val selectedCar = cars[selectedCarIndex.value]
                    onAddCar(selectedCar)
                }) {
                    Text("Добавить")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

fun navigateToCarInfoActivity(
    context: Context,
    carID: String,
) {
    val intent = Intent(context, CarInfoActivity::class.java)
    intent.putExtra("carID", carID)
    context.startActivity(intent)
}
