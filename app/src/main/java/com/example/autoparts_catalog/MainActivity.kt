package com.example.autoparts_catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autoparts_catalog.ui.theme.AutoParts_CatalogTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.serialization.Serializable
import java.util.concurrent.Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutoParts_CatalogTheme {
                Screen()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Screen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "searchScreen") {
            composable("searchScreen") { SearchScreen(innerPadding) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                onClick = { navController.navigate("searchScreen") }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
                    Text("Поиск")
                }
            }

            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                onClick = { navController.navigate("searchScreen") }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
                    Text("Автомобили")
                }
            }

            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                onClick = { navController.navigate("searchScreen") }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
                    Text("Избранное")
                }
            }
        }
    }
}

@Composable
fun SearchScreen(innerPudding: PaddingValues) {
    val query = remember { mutableStateOf("") }
    val searchResult = null

    Box(
        modifier = Modifier
            .padding(innerPudding)
    ) {
        SearchTextField(query.value, onQueryChange = { query.value = it })
        Column{
            if (searchResult.value.isNotEmpty()) {

            }
        }
    }
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit
    ) {

    OutlinedTextField(
        value = query,
        onValueChange = { onQueryChange },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.LightGray),
        placeholder = { Text(text = "Поиск...") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")}
    )
}

@Serializable
data class Parts (
    val article: String,
    val carID: String,
    val description: String,
    val name: String
)

class RepositoryParts() {
    private val db = FirebaseFirestore.getInstance()

    fun getParts(): Flow<List<Parts>> {
        return db.collection("parts")
            .addSnapshotListener() { querySnapshot, _ ->
                if (querySnapshot != null) {
                   val parts = querySnapshot.documents.map { document ->
                       document.toObject(Parts::class.java)!!
                   }
                    emit(parts)
                }
            }
            .flowOn(Dispatchers.IO)
    }
}

class SearchViewModel : ViewModel() {

}
