package com.example.autoparts_catalog.ui.elements

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.autoparts_catalog.models.Parts
import com.example.autoparts_catalog.viewmodels.FavouritesListViewModel
import com.example.autoparts_catalog.views.navigateToPartInfoActivity
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PartItem(part: Parts, favouritesListViewModel: FavouritesListViewModel) {
    val context = LocalContext.current
    val favourites by favouritesListViewModel.favourites.observeAsState(emptyList())
    val isFavourite = favourites.contains(part)

    LaunchedEffect(key1 = part.article) {
        favouritesListViewModel.loadFavourites()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .clickable {
                Log.d("PartItem", "Clicked on part: ${part.article}")
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
        IconButton(
            onClick = {
                if (isFavourite) {
                    favouritesListViewModel.removeFavourite(part)
                } else {
                    favouritesListViewModel.addFavourite(part)
                }
            }
        ) {
            Icon(
                imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Add to Favourites"
            )
        }
    }
}
