package com.userapp.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.userapp.model.ProductItem

@Composable
fun ProductCard(product: ProductItem, modifier: Modifier = Modifier) {
    val imageUrl = product.cover.orEmpty()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            if (imageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image",
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Text(
                text = product.name.ifEmpty { "No name" },
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
