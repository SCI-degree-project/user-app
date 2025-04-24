package com.userapp.ui

import androidx.compose.foundation.Image
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
import com.userapp.model.Product

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
