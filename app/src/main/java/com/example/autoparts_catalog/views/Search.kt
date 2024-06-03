package com.example.autoparts_catalog.views

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.autoparts_catalog.activity.PartInfoActivity
import com.example.autoparts_catalog.models.Parts
import com.example.autoparts_catalog.viewmodels.SearchViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage


@Composable
fun SearchScreen(innerPudding: PaddingValues, viewModel: SearchViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val parts = viewModel.parts.collectAsState()

    Column(modifier = Modifier.padding(innerPudding)) {
        SearchTextField {
            searchQuery = it
            viewModel.onSearchQueryChanged(it)
        }

        LazyColumn {
            items(parts.value) { part ->
                PartItem(part)
            }
        }
    }
}

@Composable
fun SearchTextField(onSearch: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearch(it)
        },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(Color.LightGray),
        placeholder = { Text(text = "Поиск...") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(searchQuery)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        singleLine = true
    )
}


@Composable
fun PartItem(part: Parts) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .clickable {
                navigateToPartInfoActivity(context, part.article)
            }
    ) {
        CoilImage(imageModel = { part.image },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit
            ),
            modifier = Modifier
                .size(70.dp)
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(5.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text("${part.name} - ${part.article}", modifier = Modifier.weight(1f))
    }
}

fun navigateToPartInfoActivity(context: Context, partArticle: String) {
    val intent = Intent(context, PartInfoActivity::class.java)
    intent.putExtra("article", partArticle)
    Log.d("navigateToPartInfo", "Starting PartInfoActivity with article: $partArticle")
    Log.d("navigateToPartInfo", "Intent: $intent")
    Log.d("navigateToPartInfo", "Context: $context")
    context.startActivity(intent)
}
